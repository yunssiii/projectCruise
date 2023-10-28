package com.cruise.project_cruise.quartz.config;

import com.cruise.project_cruise.controller.crew.CrewController;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzJobListner implements JobListener {
    //Job 실행 전 후에 event를 걸어주는 역할을 담당한다.

    final Logger logger = LoggerFactory.getLogger(CrewController.class); // 로그

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        logger.info("[Quartz] job 수행 전");
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        logger.info("[Quartz] job 중단");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        logger.info("[Quartz] job 수행 완료");
    }
}
