package com.cruise.project_cruise.controller.crew;

import com.cruise.project_cruise.controller.HomeController;
import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import com.cruise.project_cruise.service.CrewDetailService;
import com.cruise.project_cruise.service.CrewSettingService;
import com.cruise.project_cruise.service.MypageService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Autowired
    private CrewSettingService crewSettingService;

    final Logger logger = LoggerFactory.getLogger(CrewController.class); // 로그

// red 오류페이지
    @RequestMapping("/wrongAccess")
    public ModelAndView wrongAccess() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("crew/wrongAccess");

        return mav;
    }

// red 크루 상세페이지


    // green 크루 상세페이지 출력
    @RequestMapping(value = "")
    public ModelAndView crewMain(HttpSession session, HttpServletRequest request) throws Exception {



        // 크루 상세페이지 메인화면
        ModelAndView mav = new ModelAndView();

        int crewNum = Integer.parseInt(request.getParameter("crewNum"));
        CrewDTO dto = crewDetailService.getCrewData(crewNum);


        /**
         * TODO
         * 0. 내가 크루원일 때만 해당 페이지에 들어올 수 있게 하기
         * 1. 크루 소식조회
         * 2. crewNum에 해당하는 크루 기본정보 데이터 - 완료
         * 3. 거래내역 조회
         * 4. 회비내역 조회
         * 5. 납입기능
         * 6. 일정 간편조회
         */

        // 세션에서 접속한 유저의 이메일 받기
        String userEmail = (String) session.getAttribute("email");
        logger.info(userEmail + " 크루 상세페이지 접속");


        // bold 0. 잘못된 crewNum으로 들어갔을 때
        if (dto == null) {

            logger.info( userEmail + " 잘못된 크루 경로에 접근");
            mav.addObject("status", "wrongCrewAccess");
            mav.setViewName("crew/wrongAccess");

            return mav;
        }

        // bold 0. 크루원만 해당크루 상세페이지에 접속가능하게 처리하기 - 완료

        // 2. 선원 테이블에서 이메일이 해당 이메일이고 crewnum이 해당 num인 데이터 찾기
        if (userEmail == null || userEmail.isEmpty()) {
            logger.info( "로그인하지 않은 사용자가 [" + crewNum + " - " + dto.getCrew_name() + "] 에 접근");
            mav.addObject("status", "logout");
            mav.setViewName("crew/wrongAccess");

            return mav;
        }

        if (!crewDetailService.isMember(crewNum, userEmail)) {
            logger.info(crewNum + " - " + dto.getCrew_name() + "에 " + userEmail + " 접근 실패");
            mav.addObject("status", "notMember");
            mav.setViewName("crew/wrongAccess");

            return mav;
        }

        // bold 0. 항해 중단 크루일 시 항해중단 크루페이지로 이동하기
        if(dto.getCrew_deldate()!=null && !dto.getCrew_deldate().equals("")) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String delDecideDateStr = dto.getCrew_deldate(); // 항해 중단 결정일자 불러오고
            Date delDecideDate = dateFormat.parse(delDecideDateStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(delDecideDate);
            calendar.add(Calendar.DATE, 3);
            
            Date delConfirmDate = calendar.getTime();
            Date today = new Date();
            
            int dateCompare = delConfirmDate.compareTo(today);
                // 결과가 음수면 delConfirmDate이 더 이전 (삭제 후)
                // 결과가 0이면 같음
                // 결과가 양수면 today가 더 이전 (삭제 전)

            if(dateCompare<=0) {
                logger.info(userEmail + " 삭제된 크루 경로에 접근");
                mav.addObject("status", "wrongCrewAccess");
                mav.setViewName("crew/wrongAccess");
                return mav;
            }

            logger.info(crewNum + " - " + dto.getCrew_name() + "은 항해중단 유예 중...");

            mav.setViewName("redirect:/crew/setting/sailingStopCrew?crewNum=" + crewNum);
            return mav;
        }

        // bold 0. 크루 캡틴에게만 크루관리 뜨게 하기 - 완료
        if (crewDetailService.isCaptain(crewNum, userEmail)) {
            logger.info(crewNum + " - " + dto.getCrew_name() + "에 선장 " + userEmail + " 접속");
            mav.addObject("isCaptain", "true");
        } else {
            logger.info(crewNum + " - " + dto.getCrew_name() + "에 선원 " + userEmail + " 접속");
            mav.addObject("isCaptain", "false");
        }




        // TODO 1. 크루 소식조회

        // bold 2. 크루 기본정보 데이터 - 완료
        String captainName = crewDetailService.getCaptainName(dto.getCaptain_email());

        // 3. 날짜 데이터 ~년, ~월 ~일 형태로 바꾸어주기
        String fullCreatedDate = dto.getCrew_created();
        String[] createdDate = new String[3];

        createdDate[0] = fullCreatedDate.substring(0, 4);
        createdDate[1] = fullCreatedDate.substring(5, 7);
        createdDate[2] = fullCreatedDate.substring(8, 10);

        // 4-1. 오픈뱅킹 대용 테이블에서 계좌 잔액 가지고오기
        int crewAccountBalance = crewDetailService.getAccountBalance(dto.getCrew_accountid());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String crewAccountBalanceStr = decimalFormat.format(crewAccountBalance);

        // 4-2. 목표금액, 달성율
        int achievePer = (int) (((double) crewAccountBalance / (double) dto.getCrew_goal()) * 100);
        // int 는 정수이기 때문에 나눗셈의 소숫점 결과값을 얻으려면 double로 형변환 해주어야 함.
        String crewGoal = decimalFormat.format(dto.getCrew_goal());

        // TODO 거래내역 조회

        // TODO 회비내역 조회

        // bold 납입기능 양식 출력 - 완료
        // 접속한 유저의 선원 정보 가지고오기
        Map<String, Object> crewUserInfo = crewDetailService.getCrewUserInfo(crewNum, userEmail);

        // 숫자로 표기해야할 부분.. 따로 넘겨주기 (계산해야하니까)
        int userMustPayCount = Integer.parseInt(String.valueOf(crewUserInfo.get("MUST_PAYCOUNT")));
        int userRealPayCount = Integer.parseInt(String.valueOf(crewUserInfo.get("REAL_PAYCOUNT")));
        int userTotalPay = Integer.parseInt(String.valueOf(crewUserInfo.get("TOTAL_PAY")));

        // 접속한 유저의 등록된 계좌 들고오기
        List<MyAccountDTO> userAccountList = crewDetailService.getUserAccountList(userEmail);


        // TODO 일정 간편조회

        // 데이터 넘겨주기
        // 크루 기본정보 관련
        mav.addObject("dto", dto);
        mav.addObject("crewNum", crewNum);
        mav.addObject("captainName", captainName);
        mav.addObject("createdDate", createdDate);
        mav.addObject("crewAccountBalance", crewAccountBalanceStr);
        mav.addObject("achievePer", achievePer);
        mav.addObject("crewGoal", crewGoal);

        // 크루 유저 리스트
        mav.addObject("memberList",crewSettingService.getCrewMemberList(crewNum));

        // 납입기능 폼 데이터
        mav.addObject("crewUserInfo", crewUserInfo);
        mav.addObject("userMustPayCount", userMustPayCount);
        mav.addObject("userRealPayCount", userRealPayCount);
//            mav.addObject("userTotalPay", userTotalPay);
        mav.addObject("userAccountList", userAccountList);

        mav.setViewName("crew/crewmain");

        return mav;
    }

    // green 납입 처리하기
    @RequestMapping(value = "/paymentFee")
    public void paymentFee(@RequestParam("crewNum") int crewNum, @RequestParam("userEmail") String userEmail,
                           @RequestParam("payment") int payment, @RequestParam("payCount") int payCount) throws Exception {

        // OpenBankDTO를 업데이트 해야합니다.
        // crewNum이랑 userEmail, 납입횟수를 받아서
        // 그 행에 해당하는 실제납입횟수에 +n 합시다
        crewDetailService.updateCrewMemberPayment(crewNum, userEmail, payment, payCount);
        CrewDTO dto = crewDetailService.getCrewData(crewNum);
        logger.info("[" + crewNum + " - " + dto.getCrew_name() + "] " + userEmail + "이 " + payCount + "회 (" + payment + "원) 납입");
    }


    // green 회원 탈퇴 진행
    @RequestMapping(value = "/crewExitOK")
    public int crewExit(@RequestParam("crewNum") int crewNum, @RequestParam("userEmail") String userEmail) throws Exception {
        int exitSuccess = 0;

        CrewDTO dto = crewDetailService.getCrewData(crewNum);
        logger.info("[" + crewNum + " - " + dto.getCrew_name() + "] " + userEmail + "의 크루 탈퇴 시도");
        // 선장인지 선원인지 먼저 확인
        boolean isCaptain = crewDetailService.isCaptain(crewNum,userEmail);

        if(isCaptain) {
            logger.info("크루의 선장은 탈퇴할 수 없음.");
            return exitSuccess;
        }

        crewDetailService.deleteCrewMember(userEmail, crewNum);
        exitSuccess = 1;
        logger.info("[" + crewNum + " - " + dto.getCrew_name() + "] " + userEmail + " 크루 탈퇴 완료");

        return exitSuccess;
    }

}