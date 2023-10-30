package com.cruise.project_cruise.quartz.jobs;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.service.CrewDetailService;
import com.cruise.project_cruise.service.CrewSettingService;
import com.cruise.project_cruise.service.MypageService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@PersistJobDataAfterExecution
public class CrewPaydateJob implements Job {

    @Autowired
    private MypageService mypageService;
    @Autowired
    private CrewDetailService crewDetailService;
    @Autowired
    private CrewSettingService crewSettingService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            /**
             *  1. JOB의 내용
             *      - crew_member 테이블에 납입의무횟수 +1하기
             *      - myAlert에 납입일 알림 DB 추가하기
             *  2. Trigger의 내용은?
             *     - crew.crew_paydate에서 설정한 ‘일’에 해당하는 날마다 실행하기
             */

            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            int crewNum = (int)dataMap.get("crewNum");
            CrewDTO crewDTO = crewDetailService.getCrewData(crewNum);
            String crewName = crewDTO.getCrew_name();

            log.info(crewNum + " / " +crewName + " Crew 납입일 JOB 시작...");
            log.info("dataMap executeCount : {} ",dataMap.get("executeCount"));

            crewDetailService.updateCrewMustPayCount(crewNum); // 납입일 때 납입의무횟수 +1
            log.info(crewNum + " / " +crewName + " Crew 납입일 JOB - CREW_MEMBER.MUST_PAYCOUNT 업데이트 완료...");

            // 알림넣기
            // 1. 알림 넘버
            int alertNum = mypageService.maxMyalertNum() + 1;

            // 2. 알림 울릴 날짜
            Date today = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = dateFormat.format(today);

            // 3. 알림 받을 이메일
            List<Map<String,String>> memberList = crewSettingService.getCrewMemberList(crewNum);

            // 4. 알림 메시지
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int todayMonth = calendar.get(Calendar.MONTH) +1;
            String alertContent = todayMonth + "월 납입일 입니다.";
            int boardNum =0;

            // 5. insert 하기
            for(Map<String,String> member : memberList) {
                String userEmail = member.get("MEM_EMAIL");
                log.info(userEmail + " 에게 납입일 알림 전송");
                mypageService.insertMyAlert(alertNum,crewNum,"납입일",alertContent,todayStr,userEmail,boardNum);
            }
            log.info(crewNum + " / " +crewName + " Crew 납입일 JOB - MY_ALERT.MUST_PAYCOUNT 업데이트 완료...");
            log.info(crewNum + " / " +crewName + " Crew 납입일 JOB 완료...");


        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
