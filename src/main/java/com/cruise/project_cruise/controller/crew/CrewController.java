package com.cruise.project_cruise.controller.crew;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.service.CrewDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequestMapping(value="/crew")
@RestController
public class CrewController {

    /**
     * TODO [CrewController] : 만들어야 할 메소드 목록
     * 1. 메인화면 출력
     * 2. 크루관리페이지 출력
     */

    @Autowired
    private CrewDetailService crewDetailService;

    @RequestMapping(value="")
    public ModelAndView crewMain(HttpServletRequest request) throws Exception {
        // 크루 상세페이지 메인화면
        ModelAndView mav = new ModelAndView();
        /**
         * TODO
         * 0. 내가 크루 원일 때만 해당 페이지에 들어올 수 있게 하기
         * 1. 크루 소식조회
         * 2. crewNum에 해당하는 크루 기본정보 데이터
         * 3. 거래내역 조회
         * 4. 회비내역 조회
         * 5. 납입기능
         * 6. 일정 간편조회
         */

        // TODO 크루원만 해당크루 상세페이지에 접속가능하게 처리하기

        // TODO 크루 소식조회

        // 크루 기본정보 데이터 - 완료
            // 1. 크루 데이터 가지고오기
            int crewNum = Integer.parseInt(request.getParameter("crewNum"));
            CrewDTO dto = crewDetailService.getCrewData(crewNum);

            // 2. 선장 이름 데이터 가지고오기
            String captainName = crewDetailService.getCaptainName(dto.getCaptain_email());

            // 3. 날짜 데이터 ~년, ~월 ~일 형태로 바꾸어주기
            String fullCreatedDate = dto.getCrew_created();
            String[] createdDate = new String[3];

            createdDate[0] = fullCreatedDate.substring(0,4);
            createdDate[1] = fullCreatedDate.substring(5,7);
            createdDate[2] = fullCreatedDate.substring(8,10);

            // 4-1. 오픈뱅킹 대용 테이블에서 계좌 잔액 가지고오기
            int crewAccountBalance = crewDetailService.getAccountBalance(dto.getCrew_accountid());
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String crewAccountBalanceStr = decimalFormat.format(crewAccountBalance);

            // 4-2. 목표금액, 달성율
            int achievePer = (crewAccountBalance/dto.getCrew_goal())*100;
            String crewGoal = decimalFormat.format(dto.getCrew_goal());


        // TODO 거래내역 조회

        // TODO 회비내역 조회

        // TODO 납입기능 양식 출력

        // TODO 일정 간편조회

        // 데이터 넘겨주기
        mav.addObject("dto",dto);
        mav.addObject("captainName",captainName);
        mav.addObject("createdDate",createdDate);
        mav.addObject("crewAccountBalance", crewAccountBalanceStr);
        mav.addObject("achievePer", achievePer);
        mav.addObject("crewGoal", crewGoal);

        mav.setViewName("crew/crewmain");

        return mav;
    }

    // 탈퇴 진행
    @RequestMapping(value="/crewExit")
    public ModelAndView crewExit() throws Exception {
        ModelAndView mav = new ModelAndView();

        // FIXME 현재 로그인한 멤버의 이메일, CrewNum 가져오기

        String cmemEmail = ""; // 가상 이메일
        int crewNum = 0; // 가상 크루넘버

        crewDetailService.deleteCrewMember(cmemEmail,crewNum);
        mav.setViewName("redirect:/crewmain");

        return mav;
    }

    @RequestMapping(value="/setting")
    public ModelAndView crewSetting() throws Exception {
        ModelAndView mav = new ModelAndView();

        // FIXME Post 방식으로 CrewNum을 넘겨받기

        mav.setViewName("crew/crewSetting");

        return mav;
    }

}
