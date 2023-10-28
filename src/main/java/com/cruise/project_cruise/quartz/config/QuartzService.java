package com.cruise.project_cruise.quartz.config;

import com.cruise.project_cruise.controller.crew.CrewController;
import com.cruise.project_cruise.quartz.jobs.QuartzJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;


import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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

            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("executeCount",1);
            // 앞서 실행횟수를 체크하는 변수로 설정해줬던 executeCount 를 1로 설정해 담아주고,
            paramsMap.put("crewNum",1); // 일단 임의로 넣기. 1번 크루 알림에 넣어보자
            paramsMap.put("cAlertContent","Quartz 테스트");
            paramsMap.put("cAlertAssort","테스트");

            // 그리고 date 를 로그에 출력하도록 해주었으니까 그것도 넣어주자!
            LocalDateTime now = LocalDateTime.now();
            paramsMap.put("date",now);

            //Job을 생성해 Scheduler에 등록하자
            addJob(QuartzJob.class, "QuartzJob", "TestJobs","Quartz Job 입니다", paramsMap, "0/5 * * * * ?"); // 5초간격으로 실행한다는 뜻


        }catch (Exception e) {
            log.error("addJob error : {}", e);
        }
    }

    //Job 추가하는 메소드
    public <T extends Job> void addJob(Class<? extends Job> job , String key, String group, String desc, Map<String,Object> paramsMap, String cron) throws SchedulerException, SchedulerException {
        JobDetail jobDetail = buildJobDetail(job,group,key,desc,paramsMap);
        Trigger trigger = buildCronTrigger(cron); // cron으로 트리거 생성하기
        if(scheduler.checkExists(jobDetail.getKey())) scheduler.deleteJob(jobDetail.getKey());
        scheduler.scheduleJob(jobDetail,trigger);
    }

    //JobDetail 생성하는 메소드
    public <T extends Job> JobDetail buildJobDetail(Class<? extends Job> job, String group, String key, String desc, Map<String,Object> paramsMap) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(paramsMap);

        return JobBuilder
                .newJob(job) // job 클래스를 넣어준다.
                .withIdentity(key, group) // scheduler 내에 해당 Job을 구분할 고유 식별키와, group을 지정한다.
                .withDescription(desc) // Job에 대한 설명(Description)을 설정하는 데 사용한다.
                .usingJobData(jobDataMap) // JobDataMap에 구성해준 params 들을 넣어준다.
                .build();
    }

    //Trigger 생성
    private Trigger buildCronTrigger(String cronExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build();
    }



}
