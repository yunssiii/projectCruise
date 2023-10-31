package com.cruise.project_cruise.quartz.jobs;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.ScheduleDTO;
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

@Slf4j
@Component
@PersistJobDataAfterExecution
public class CrewPaydateScheduleJob implements Job {

    @Autowired
    private CrewDetailService crewDetailService;
    @Autowired
    private CrewSettingService crewSettingService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            /* 일정 추가하기*/

            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            int crewNum = (int)dataMap.get("crewNum");
            CrewDTO crewDTO = crewDetailService.getCrewData(crewNum);
            String crewName = crewDTO.getCrew_name();

            int scheNum = 0;
            scheNum = crewSettingService.getScheMaxNum()+1;

            Date today = new Date();
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(today);
            // 매월 납입일
            int payDate = crewDTO.getCrew_paydate();
            int todayMonthLastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            if(todayMonthLastDate<=payDate) {
                payDate = todayMonthLastDate;
            }

            calendar.set(Calendar.DAY_OF_MONTH,payDate);

            Date payDateObj = calendar.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(payDateObj);

            ScheduleDTO dto = new ScheduleDTO();
            dto.setSche_num(scheNum);
            dto.setCrew_num(crewNum);
            dto.setSche_title("[" + crewName + "] 납입일");
            dto.setSche_assort("#FF8383");
            dto.setSche_alldayTF("true");
            dto.setSche_start(formattedDate);
            dto.setSche_end(formattedDate);

            crewSettingService.insertCrewSche(dto);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
