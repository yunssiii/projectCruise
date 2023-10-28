package com.cruise.project_cruise.quartz.jobs;

import com.cruise.project_cruise.controller.crew.CrewController;
import com.cruise.project_cruise.service.CrewAlertService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@PersistJobDataAfterExecution // Job이 시행중일 때 JobDataMap을 변경할 때 사용한다.
public class QuartzJob implements Job {


    @Autowired
    private CrewAlertService crewAlertService;
    final Logger logger = LoggerFactory.getLogger(CrewController.class); // 로그

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Quartz Job 시작 ");

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        logger.info("dataMap date : {} ",dataMap.get("date"));
        logger.info("dataMap executeCount : {} ",dataMap.get("executeCount"));

        // executeCountsms Job의 실행횟수를 뜻함!
        int cnt = (int) dataMap.get("executeCount");
        dataMap.put("executeCount",++cnt); // 실행횟수를 받아서 횟수 +1을 해준다.
        // TriggerListener 에서 vetoJobExecution 를 통해
        // exectueCount가 2 이상이 되면 Job을 중지하도록 해주었음!
        // 그러므로 이 Job은 한번 시행된 후, executeCount가 2가 되고, 중지될 것.

        // 실행할 작업 여기 적기
        // 예시 - 현재 시간으로 crewAlert 넣어보기
        try {
            int maxNum = crewAlertService.cAlertMaxNum() +1;

            LocalDate today = LocalDate.now();
            String todayMonth = Integer.toString(today.getMonthValue());
            String todayDate = Integer.toString(today.getDayOfMonth());

            if(today.getMonthValue()<10) {
                todayMonth = '0' + todayMonth;
            }
            if(today.getDayOfMonth()<10) {
                todayDate = '0' + todayDate;
            }

            String todayStr = today.getYear() + "-" + todayMonth + "-" +todayDate;

            crewAlertService.insertCrewAlert(
                    maxNum,
                    (int)dataMap.get("crewNum"),
                    (String)dataMap.get("cAlertAssort"),
                    (String)dataMap.get("cAlertContent"),
                    todayStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
