package com.cruise.project_cruise.quartz.config;

import com.cruise.project_cruise.controller.crew.CrewController;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzTriggerListener implements TriggerListener {
    // 트리거 실행 전 후에 event를 걸어주는 역할을 담당한다.

    final Logger logger = LoggerFactory.getLogger(CrewController.class); // 로그

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        logger.info("[Quartz] Trigger 실행");
    }

    /**
     * @Content : 결과가 true이면 JobListner - jobExecutionVetoed(Job중단) 실행
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        // Job이 멈추는 조건을 설정하는 곳인 듯 하다.
        // 여기 조건이 true가 되면 멈추고, false가 되면 Job이 실행됨.
        // 특정 상황에서 Job의 실행을 막기 위해 사용될 수 있으며,
        // 예를 들어 특정 조건이 충족되지 않을 때 Job을 실행하지 않도록 할 때 유용
        // 그럼 만약 납입일에 관한거면.. deldate가 null이면 시행하지 못하도록 여기서 막아줄 수 있나?
        logger.info("[Quartz] Trigger 상태 체크");
        JobDataMap map = context.getJobDetail().getJobDataMap();

        int executeCount = 1;
        if(map.containsKey("executeCount")) {
            executeCount = (int)map.get("executeCount");
        }
        return executeCount>=2;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        logger.info("[Quartz] Trigger 성공");
    }
}
