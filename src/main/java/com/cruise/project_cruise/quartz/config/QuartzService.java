package com.cruise.project_cruise.quartz.config;

import com.cruise.project_cruise.controller.crew.CrewController;
import com.cruise.project_cruise.quartz.jobs.QuartzJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j // 로그 어노테이션
@Configuration
@RequiredArgsConstructor
public class QuartzService {

    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            // 스케줄러를 초기화하기
            scheduler.clear(); // DB도 초기화된다. 모든 예약된 작업과 트리거가 삭제된다.
            // TODO 최종적으론 이거 빼주기!

            // 만들어뒀던 리스너들을 등록해준다.
            scheduler.getListenerManager().addJobListener(new QuartzJobListner());
            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListener());

            // QuartzJob에서 설정해준 Parameter를 생성해주자.
            /*
            crewAlertService.insertCrewAlert(
                    maxNum,
                    (int)dataMap.get("crewNum"),
                    (String)dataMap.get("cAlertAssort"),
                    (String)dataMap.get("cAlertContent"),
                    String.valueOf(LocalDateTime.now()));
                    이렇게 해줬었음.
             */

        }catch (Exception e) {
            log.error("addJob error : {}", e);
        }
    }



    //cron식 Job
    public <T extends Job> void addCronJob(Class<? extends Job> job , String key, String desc, Map<String,Object> paramsMap, String cron) throws SchedulerException, SchedulerException {
        JobDetail jobDetail = buildJobDetail(job,key,desc,paramsMap);
        Trigger trigger = buildCronTrigger(cron); // cron으로 트리거 생성하기
        if(scheduler.checkExists(jobDetail.getKey())) scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail,trigger);
    }

    //매달 특정일자에 실행되는 Job
    public <T extends Job> void addMonthlyJob(Class<? extends Job> job , String key, String desc, Map<String,Object> paramsMap, int monthlyDate) throws SchedulerException, SchedulerException {
        JobDetail jobDetail = buildJobDetail(job,key,desc,paramsMap);
        Trigger trigger = buildMonthlyTrigger(monthlyDate);
        if(scheduler.checkExists(jobDetail.getKey())) scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail,trigger);
    }

    // 한 번 실행되는 Job
    public <T extends Job> void addSimpleOnceJob(Class<? extends Job> job , String key, String desc, Map<String,Object> paramsMap, Date date) throws SchedulerException, SchedulerException {
        JobDetail jobDetail = buildJobDetail(job,key,desc,paramsMap);
        Trigger trigger = buildOnceSimpleTrigger(date); // cron으로 트리거 생성하기
        if(scheduler.checkExists(jobDetail.getKey())) scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail,trigger);
    }

    // 반복 실행되는 Job
    public <T extends Job> void addSimpleRepeatJob(Class<? extends Job> job , String key, String desc, Map<String,Object> paramsMap, Date date,int intervalInMilliseconds, int repeatCount) throws SchedulerException, SchedulerException {
        JobDetail jobDetail = buildJobDetail(job,key,desc,paramsMap);
        Trigger trigger = buildRepeatSimpleTrigger(date, intervalInMilliseconds, repeatCount); // cron으로 트리거 생성하기
        if(scheduler.checkExists(jobDetail.getKey())) scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail,trigger);
    }

    //JobDetail 생성하는 메소드
    public <T extends Job> JobDetail buildJobDetail(Class<? extends Job> job, String key, String desc, Map<String,Object> paramsMap) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(paramsMap);

        return JobBuilder
                .newJob(job) // job 클래스를 넣어준다.
                .withIdentity(key) // scheduler 내에 해당 Job을 구분할 고유 식별키와, group을 지정한다.
                .withDescription(desc) // Job에 대한 설명(Description)을 설정하는 데 사용한다.
                .usingJobData(jobDataMap) // JobDataMap에 구성해준 params 들을 넣어준다.
                .build();
    }

    //Cron Trigger 생성
    private Trigger buildCronTrigger(String cronExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
    }

    // 매달 실행되는 Trigger 생성
    private Trigger buildMonthlyTrigger(int monthlyDate) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(makeMonthlyCronExpression(monthlyDate)))
                .build();
    }

    //Simple Trigger 생성
    private Trigger buildOnceSimpleTrigger(Date jobStartDateObj) {

        return TriggerBuilder.newTrigger()
                .startAt(jobStartDateObj)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(0)) // 한 번 실행되고 종료된다.
                .build();
    }

    private Trigger buildRepeatSimpleTrigger(Date jobStartDateObj,int intervalInMilliseconds, int repeatCount) {

        return TriggerBuilder.newTrigger()
                .startAt(jobStartDateObj)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMilliseconds(intervalInMilliseconds)
                        .withRepeatCount(repeatCount-1)) // 총 실행 횟수
                .build();
    }

    private String makeMonthlyCronExpression(int monthlyDate) {
        String cronStr = "0 0 12 "+ monthlyDate +" 1/1 ? *";

        Date todayDateObj = new Date();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(todayDateObj);
        int lastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if(lastDate<monthlyDate) { // 만약 monthlyDate 보다 lastdate가 작다면 (31일로 설정되어있는데 마지막 날짜가 30일, 29일이거나..)
            monthlyDate = lastDate; // paydate를 납입날짜 대신 그 달의 마지막 날짜로 대신해서 설정
        }

        return cronStr;
    }



}
