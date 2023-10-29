package com.cruise.project_cruise.quartz.jobs;


import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PersistJobDataAfterExecution
public class StoppingCrewJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        log.info("[StoppingCrewJob] JOB start");
        /**
         * TODO 항해 중단 신청 시 시행해야할 JOB들
         * 1. Job의 내용은?
         *     green 1. crew에 연관된 모든 스케줄링을 `중단`하는 JOB
         *         - 납입일 때 myalert DB 추가 + 소켓전송 하는 JOB을 중단
         *         - 납입일 때 crew_member.must_paydate 에 +1 JOB을 중단
         *     2. crew를 3일 뒤에 삭제하는 JOB
         * 2. Trigger의 내용은?
         *     1. 항해중단 신청 시!
         *     2. deldate로부터 3일 뒤
         * 3. 어떤게 필요한가?
         *     - 스케줄링 등록하기
         *         - 크루가 만들어질 때 등록될 것
         *     - 스케줄링 업데이트하기
         *         - 크루 정보를 수정할 때 시행될 것
         *     - 스케줄링 중단하기
         *         - 항해중단 시 중단되어야 함
         *     - 스케줄링 취소하기
         *         - 크루가 삭제될 때 취소되어야 함
         */

        // TODO crew와 연관된 모든 스케줄링을 중단하는 JOB



    }
}
