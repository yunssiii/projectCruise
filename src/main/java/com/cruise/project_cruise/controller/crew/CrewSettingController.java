package com.cruise.project_cruise.controller.crew;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.dto.ScheduleDTO;
import com.cruise.project_cruise.dto.UserDTO;
import com.cruise.project_cruise.quartz.config.QuartzService;
import com.cruise.project_cruise.quartz.jobs.CrewDeleteJob;
import com.cruise.project_cruise.quartz.jobs.CrewPaydateJob;
import com.cruise.project_cruise.quartz.jobs.CrewPaydateScheduleJob;
import com.cruise.project_cruise.service.CrewAlertService;
import com.cruise.project_cruise.service.CrewDetailService;
import com.cruise.project_cruise.service.CrewSettingService;
import com.cruise.project_cruise.service.MypageService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequestMapping(value="/crew/setting")
@RestController
public class CrewSettingController {

    @Autowired
    private CrewDetailService crewDetailService;
    @Autowired
    private CrewSettingService crewSettingService;
    @Autowired
    private MypageService mypageService;
    @Autowired
    private CrewAlertService crewAlertService;
    @Autowired
    private QuartzService quartzService;
    @Autowired
    private Scheduler scheduler;

    private final String cruiseUrl = "http://192.168.16.27:8082/";

    // red 풀캘린더 데이터 전달 URL
    // - main, setting, mypage에서 공동 사용
    @RequestMapping("/loadCrewSchedule")
    @ResponseBody
    public List<Map<String,Object>> loadCrewSchedule (@RequestParam("crewNum") int crewNum) throws Exception {
        List<ScheduleDTO> crewScheList = crewSettingService.getCrewScheList(crewNum);

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        HashMap<String, Object> calHash = new HashMap<>();

        boolean allDay = false;

        for (int i = 0; i < crewScheList.size(); i++) {
            calHash.put("id",crewScheList.get(i).getSche_num());
            calHash.put("title", crewScheList.get(i).getSche_title());
            calHash.put("start", crewScheList.get(i).getSche_start());

            // Allday가 true일 경우, 시작일과 마감일을 18~20일로 잡아도 달력상엔 18~19일로 표시됨.
            // 이를 방지하기 위함
            SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date endDateObj = endDateFormat.parse(crewScheList.get(i).getSche_end());
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDateObj);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            endDateObj = cal.getTime();

            String AlldayEndDateString = endDateFormat.format(endDateObj);

            if(crewScheList.get(i).getSche_alldayTF().equals("true")){
                allDay = true;
                calHash.put("end", AlldayEndDateString);
            } else {
                allDay = false;
                calHash.put("end", crewScheList.get(i).getSche_end());
            }
            calHash.put("allDay", allDay);
            calHash.put("color", crewScheList.get(i).getSche_assort());
            calHash.put("textColor", "#FFFFFF");

            jsonObject = new JSONObject(calHash);
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

// red 크루 관리

    // green 크루 관리페이지 메인
    @RequestMapping(value="")
    public ModelAndView crewSetting(HttpSession session, HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("cruiseUrl",cruiseUrl);

        // 선언부
        int crewNum = Integer.parseInt(request.getParameter("crewNum"));
        CrewDTO dto = crewDetailService.getCrewData(crewNum); // 크루 정보
        String userEmail = (String) session.getAttribute("email"); // 접속자 이메일

        List<Map<String,String>> memberList = crewSettingService.getCrewMemberList(crewNum);
        int memberCount = memberList.size(); // 크루 선원 목록
            // 크루 잔액
        int crewAccountBalance = crewDetailService.getAccountBalance(dto.getCrew_accountid());
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String crewAccountBalanceStr = decimalFormat.format(crewAccountBalance);



        // 로그인 / 로그아웃 여부 걸러내기
        if(userEmail==null||userEmail.isEmpty()) {
            log.info("로그인하지 않은 사용자가 [" + crewNum + " - " + dto.getCrew_name() + "] 관리에 접근");
            mav.addObject("status","logout");
            mav.setViewName("crew/wrongAccess");

            return mav;
        }

        log.info(userEmail + "이 [" + crewNum + " - " + dto.getCrew_name() + "] 관리에 접근");

        // 선장이 아닌 사용자를 걸러내기
        if(!crewDetailService.isCaptain(crewNum,userEmail)) {
            log.info(userEmail + "은 선장이 아님");
            mav.addObject("status","notCaptain");
            mav.setViewName("crew/wrongAccess");

            return mav;
        }


        // bold 크루 정보수정 페이지
            UserDTO crewCaptain = crewSettingService.getUser(dto.getCaptain_email()); // 선장 정보

            // 항해 일수
            Date todayDateObj = new Date(); // 오늘 날짜

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date crewCreatedDateObj = formatter.parse(dto.getCrew_created()); // Date 객체로 변환

            long dateDifference = todayDateObj.getTime() - crewCreatedDateObj.getTime() + 1; // 개설일을 1일로
            dateDifference = dateDifference / (1000 * 60 * 60 * 24);
            String crewSailingDayCount = Long.toString(dateDifference) + "일";

        // TODO bold 크루 선원탈퇴 페이지
            // 선원들 목록을 리스트로 뽑아와야해.


        // TODO bold 잔액 1/N하기 View

            // 각 선원별 account정보들을 list에 담고, 이를 다시 map에 담기
        Map<String,List<MyAccountDTO>> memberAccountMap = new HashMap<>();
            // map의 key는 그 회원의 이메일로 주고, View에서 타임리프를 통해 불러낸다.
        for(int i=0;i<memberCount;i++) {
            List<MyAccountDTO> mem_accounts = crewDetailService.getUserAccountList(memberList.get(i).get("MEM_EMAIL"));
            memberAccountMap.put(memberList.get(i).get("MEM_EMAIL"),mem_accounts);
        }

            // 크루 선원들의 총 납입횟수
        int crewMemPayCountSum = crewSettingService.getMemberPayCountSum(crewNum);
            // 1회 납입 당 분담금액
        double amountPerPaymentDouble = ((double)crewAccountBalance/(double)crewMemPayCountSum);
        amountPerPaymentDouble = Math.floor(amountPerPaymentDouble); // 혹시 모를 ... 소수점 버리기
        int amountPerPayment = (int)amountPerPaymentDouble;



        mav.addObject("dto",dto); // 크루 정보
        mav.addObject("crewAccountBalanceStr",crewAccountBalanceStr); // 크루 잔액
        mav.addObject("crewAccountBalance",crewAccountBalance); // 크루 잔액
        mav.addObject("memberList",memberList); // 선원 리스트
        mav.addObject("memberAccountMap",memberAccountMap); // 선원들 계좌리스트
        mav.addObject("crewCaptain",crewCaptain); // 선장 정보
        mav.addObject("crewSailingDayCount",crewSailingDayCount); // 항해일수
        mav.addObject("crewNum",crewNum);
        mav.addObject("amountPerPayment",amountPerPayment); // 크루 선원들의 총 납입횟수
        mav.setViewName("crew/crewSetting");

        return mav;
    }




    // green 크루 정보 수정 submit
    @RequestMapping(value="/updateCrewInfo")
    public JSONObject updateCrewInfo(
            @RequestParam("crewNum") int crewNum,
            @RequestParam("crewInfo") String crewInfo,
            @RequestParam("payDate") int payDate,
            @RequestParam("payMoney") int payMoney,
            @RequestParam("goalMoney") int goalMoney
        ) throws Exception {
        CrewDTO crewDTO = crewDetailService.getCrewData(crewNum);
        crewDTO.setCrew_num(crewNum);
        crewDTO.setCrew_info(crewInfo);
        crewDTO.setCrew_paydate(payDate);
        crewDTO.setCrew_paymoney(payMoney);
        crewDTO.setCrew_goal(goalMoney);

        crewSettingService.updateCrewInfo(crewDTO);

        HashMap<String, Object> hash = new HashMap<>();
        CrewDTO newCrewDTO = crewDetailService.getCrewData(crewNum);
        log.info("[DB] " + crewNum + "/" + newCrewDTO.getCrew_name() + " 크루 정보 DB 수정완료");

        // JOB 삭제, 수정하기
        // 1. JOB KEY 불러오기
        String jobKey = "JOB_" + crewNum + "_CrewPaydateJob";
        // 2. 등록된 job을 scheduler에서 취소하기
        scheduler.deleteJob(JobKey.jobKey(jobKey));
        // 3. job 다시 등록하기
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("executeCount",1); // 앞서 실행횟수를 체크하는 변수로 설정해줬던 executeCount 를 1로 설정해 담아주기
        paramsMap.put("crewNum",crewNum); // crewNum 담아주기

        String jobDesc = crewNum + " / " + crewDTO.getCrew_name() + "크루 납입일 Job 입니다.";
        quartzService.addMonthlyJob(CrewPaydateJob.class,jobKey,jobDesc,paramsMap,payDate);


        // 은지 - 납입일 일정 추가
        String scheJobKey = "JOB_" + crewNum + "_CrewPayDateScheduleJob";

        scheduler.deleteJob(JobKey.jobKey(scheJobKey));
        String schejobDesc = crewNum + " / " + crewDTO.getCrew_name() + "크루 납입일 일정 추가 Job 입니다.";
        quartzService.addMonthlyJob(CrewPaydateScheduleJob.class,scheJobKey,schejobDesc,paramsMap,1);


        log.info("[Quartz] " + crewNum + "/" + newCrewDTO.getCrew_name() + " 크루 납입일 JOB 수정완료");

        hash.put("newCrewInfo",newCrewDTO.getCrew_info());
        hash.put("newPayDate",newCrewDTO.getCrew_paydate());
        hash.put("newPayMoney",newCrewDTO.getCrew_paymoney());
        hash.put("newGoalMoney",newCrewDTO.getCrew_goal());

        JSONObject newCrewjsonObject = new JSONObject(hash);

        return newCrewjsonObject;
    }


// red 크루관리
    // green 크루 일정 관리
    // bold 일정 추가
    @RequestMapping(value="/addCrewSche")
    @ResponseBody
    public void addCrewSche(
            @RequestParam("crewNum") int crewNum,
            @RequestParam("scheTitle") String scheTitle,
            @RequestParam("scheAssort") String scheAssort,
            @RequestParam("scheAllDayTF") String scheAllDayTF,
            @RequestParam("scheStart") String scheStart,
            @RequestParam("scheEnd") String scheEnd
            ) throws Exception {

        String scheAssortCode = "";
        CrewDTO crewDTO = crewDetailService.getCrewData(crewNum);

        switch (scheAssort) {
            default:
            case "redSche":
                scheAssortCode = "#FF8383";
                break;
            case "greenSche":
                scheAssortCode = "#22B14C";
                break;
            case "yellowSche":
                scheAssortCode = "#FFC90E";
                break;
            case "blueSche":
                scheAssortCode = "#00A5ED";
                break;
            case "graySche":
                scheAssortCode = "#A1A1A1";
                break;
        }

        if(scheAllDayTF==null || scheAllDayTF.isEmpty()) {
            scheAllDayTF = "false";
        }

        int scheNum = crewSettingService.getScheMaxNum()+1;
        ScheduleDTO dto = new ScheduleDTO();
        dto.setSche_num(scheNum);
        dto.setCrew_num(crewNum);
        dto.setSche_title(scheTitle);
        dto.setSche_assort(scheAssortCode);
        dto.setSche_alldayTF(scheAllDayTF);
        dto.setSche_start(scheStart);
        dto.setSche_end(scheEnd);

        crewSettingService.insertCrewSche(dto);
        log.info("===================================================");
        log.info("[" + crewNum + "/" + crewDTO.getCrew_name() + " Calendar] 크루 일정 추가완료");
        log.info("---------------------------------------------------");
        log.info("[" + scheNum + "] scheTitle: " + scheTitle);
        log.info("startDate: " + scheStart);
        log.info("endDate: " + scheEnd);
        log.info("===================================================");

        // 크루 알림 추가
            // 날짜 설정
        LocalDate today = LocalDate.now();
        String todayMonth = Integer.toString(today.getMonthValue());
        String todayDate = Integer.toString(today.getDayOfMonth());

        if(today.getMonthValue()<10) {
            todayMonth = '0' + todayMonth;
        }
        if(today.getDayOfMonth()<10) {
            todayDate = '0' + todayDate;
        }

        String scheTitleSub = scheTitle.substring(0,4) + "...";
        String crewAlertContent = "[일정추가] \""+ scheTitleSub +"\" 일정이 추가되었습니다.";

        String todayStr = today.getYear() + "-" + todayMonth + "-" +todayDate;
        crewAlertService.insertCrewAlert(crewAlertService.cAlertMaxNum() + 1, dto.getCrew_num(),
                "일정", crewAlertContent, todayStr);

    }

    // bold 일정 수정
    @RequestMapping(value="/updateCrewSche")
    @ResponseBody
    public void updateCrewSche(
            @RequestParam("scheNum") int scheNum,
            @RequestParam("scheTitle") String scheTitle,
            @RequestParam("scheAssort") String scheAssort,
            @RequestParam("scheAllDayTF") String scheAllDayTF,
            @RequestParam("scheStart") String scheStart,
            @RequestParam("scheEnd") String scheEnd
    ) throws Exception {

        String scheAssortCode = "";

        switch (scheAssort) {
            default:
            case "redSche":
                scheAssortCode = "#FF8383";
                break;
            case "greenSche":
                scheAssortCode = "#22B14C";
                break;
            case "yellowSche":
                scheAssortCode = "#FFC90E";
                break;
            case "blueSche":
                scheAssortCode = "#00A5ED";
                break;
            case "graySche":
                scheAssortCode = "#A1A1A1";
                break;
        }

        if(!scheAllDayTF.equals("true")) {
            scheAllDayTF = "false";
        } else {
            scheAllDayTF = "true";
        }

        ScheduleDTO dto = new ScheduleDTO();
        dto.setSche_num(scheNum);
        dto.setSche_title(scheTitle);
        dto.setSche_assort(scheAssortCode);
        dto.setSche_alldayTF(scheAllDayTF);
        dto.setSche_start(scheStart);
        dto.setSche_end(scheEnd);

        crewSettingService.updateCrewSche(dto);

        log.info("===================================================");
        log.info("[CrewController - Setting : Calendar] 일정 수정완료");
        log.info("---------------------------------------------------");
        log.info("[" + scheNum + "] scheTitle: " + scheTitle);
        log.info("startDate: " + scheStart);
        log.info("endDate: " + scheEnd);
        log.info("===================================================");

    }

    // bold 일정 삭제
    @RequestMapping(value="/deleteCrewSche")
    @ResponseBody
    public void deleteCrewSche(@RequestParam("scheNum") int scheNum) throws Exception {
        crewSettingService.deleteCrewSche(scheNum);

        log.info("===================================================");
        log.info("[CrewController - Setting : Calendar] 일정 삭제완료");
        log.info("---------------------------------------------------");
        log.info("scheNum: " + scheNum);
        log.info("===================================================");

    }

    // green 항해 중단하기 ( 중단 결정일 데이터 삽입 )
    @RequestMapping(value="/updateDelDate")
    public ModelAndView updateDelDate(@RequestParam("crewNum") int crewNum) throws Exception {

        ModelAndView mav = new ModelAndView();
        mav.addObject("cruiseUrl",cruiseUrl);

        crewSettingService.stopSailing(crewNum); // 크루 항해 중단날짜 업데이트 한 후에
        CrewDTO dto = crewDetailService.getCrewData(crewNum); // 업데이트 된 dto 들고와주고
        log.info(dto.getCrew_name() + " 크루 항해 중단 날짜 설정 완료...");

        // bold delete JOB 3일뒤로 추가하기
        // 1. paramsMap 설정
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put("executeCount",1); // 앞서 실행횟수를 체크하는 변수로 설정해줬던 executeCount 를 1로 설정해 담아주기
        paramsMap.put("crewNum",crewNum); // crewNum 담아주기

        // 2. JOB의 Key 설정하기
        String jobKey = "JOB_" + crewNum + "_CrewDeleteJob";
        String jobDesc = crewNum + " / " + dto.getCrew_name() + "크루 삭제 Job 입니다.";

        // 3. delDate + 3 해주기
        String delDateComfirm = dto.getCrew_deldate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(delDateComfirm);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 3); // 3일 추가
        date = calendar.getTime();

        // 3. job 추가하기
        quartzService.addSimpleOnceJob(CrewDeleteJob.class,jobKey,jobDesc,paramsMap,date);

        // bold 납입일 job 삭제하기
        // 등록된 job을 scheduler에서 취소
        String payJobKey = "JOB_" + crewNum + "_CrewPaydateJob";
        scheduler.deleteJob(JobKey.jobKey(payJobKey));
        log.info("[Quartz] " + crewNum + "/" + dto.getCrew_name() + " 크루 납입일 JOB 삭제 완료");

        log.info(dto.getCrew_name() + " 크루 항해 중단...");
        log.info(dto.getCrew_name() + " 항해 중단 페이지로 리디렉트...");

        mav.setViewName("redirect:/crew/setting/sailingStopCrew?crewNum=" + crewNum);
        return mav;
    }

    // 항해를 중단 유예중인 크루에 접속하면 오는 곳
    @RequestMapping(value="/sailingStopCrew")
    public ModelAndView sailingStopCrew(@RequestParam("crewNum") int crewNum) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("cruiseUrl",cruiseUrl);
        CrewDTO dto = crewDetailService.getCrewData(crewNum); // 크루 정보 불러와서

        if(dto.getCrew_deldate()==null || dto.getCrew_deldate().equals("")) {
            log.info(crewNum + " - " + dto.getCrew_name() + "으로 이동...");
            mav.setViewName("redirect:/crew?crewNum=" + crewNum);
            return mav;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String delDecideDateStr = dto.getCrew_deldate(); // 항해 중단 결정일자 불러오고
        Date delDecideDate = dateFormat.parse(delDecideDateStr);

        mav.addObject("status","stopSailingCrew");
        mav.addObject("dto",dto);
        mav.addObject("delDecideDateStr",delDecideDateStr);

        mav.setViewName("crew/wrongAccess");
        return mav;
    }

    // 항해 중단 취소
    @RequestMapping(value="/cancelSailingStop")
    public ModelAndView cancelSailingStop(@RequestParam("crewNum") int crewNum) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("cruiseUrl",cruiseUrl);

        // bold DB관련
        // 0. deldate를 null로 바꾸는 작업하기
        crewSettingService.cancelStopSailing(crewNum);
        CrewDTO dto = crewDetailService.getCrewData(crewNum);

        // bold job 관련
        // 1. 취소할 job의 key 가져오기 (crewdeletejob)
        String jobKey = "JOB_" + crewNum + "_CrewDeleteJob";
        // 2. 등록된 job을 scheduler에서 취소하기
        scheduler.deleteJob(JobKey.jobKey(jobKey));

        // bold 삭제되었던 일정관련 job들 다시 추가하기
        Map<String, Object> paramsMap = new HashMap<>();

        paramsMap.put("executeCount",1); // 앞서 실행횟수를 체크하는 변수로 설정해줬던 executeCount 를 1로 설정해 담아주기
        paramsMap.put("crewNum",crewNum); // crewNum 담아주기

        // 1. JOB의 Key 설정하기
        String payJobKey = "JOB_" + crewNum + "_CrewPaydateJob";
        String jobDesc = crewNum + " / " + dto.getCrew_name() + "크루 납입일 Job 입니다.";

        // 2. cron식 적기
        int payDate = dto.getCrew_paydate(); // 기본적으로는 paydate로
        // 만약 30, 31일이 납입일이라면...
        // 2월 같은 경우는 실행이 되지 않거나 오류가 뜰 거고, 31일이 없는 달에도 마찬가지가 될 것
        // makeCrewPaydateCronExpression() 라는 메소드를 만들어, 크론식 대신에 makeCrewPaydateCronExpression 넣어주려고 함...

        // 3. job 추가하기
        quartzService.addMonthlyJob(CrewPaydateJob.class,payJobKey,jobDesc,paramsMap,payDate);
        log.info("[Quartz] " + crewNum + "/" + dto.getCrew_name() + " 크루 납입일 JOB 등록 완료");

        log.info(dto.getCrew_name() + " 크루 항해 중단 취소...");
        log.info(dto.getCrew_name() + " 크루 페이지로 리디렉트...");

        mav.setViewName("redirect:/crew?crewNum="+crewNum);
        return mav;
    }



// red 선원관리
    // green 월별 회비조회 - 회비 독촉 알림보내기
    @RequestMapping(value="/alertFee")
    public void alertFee(@RequestParam("crewNum") int crewNum,
                          @RequestParam("email") String email,
                          @RequestParam("sendMsg") String sendMsg) throws Exception {

        int alertNum = mypageService.maxMyalertNum() + 1;
        CrewDTO dto = crewDetailService.getCrewData(crewNum);
        String userName = crewSettingService.getUser(email).getUser_name();
        String assort = "회비요청";
        String crewName = dto.getCrew_name();
        String content = "[" + crewName + "] " + sendMsg;

        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = dateFormat.format(today);
        int boardNum =0;

        // 윤하 crewNum 추가합니다..!
        mypageService.insertMyAlert(alertNum,crewNum,assort,content,todayStr,email,boardNum);

    }



    // green 선원 강퇴하기 submit
    @RequestMapping(value="/memberBan")
    public void memberBan(@RequestParam("crewNum") int crewNum,
                          @RequestParam("email") String email) throws Exception {
        if(crewDetailService.isCaptain(crewNum,email)) {
            log.info("선장은 강퇴할 수 없습니다.");
            return;
        }

        Map<String,Object> crewMemberMap = crewDetailService.getCrewUserInfo(crewNum,email);
        String exitUserEmailSplit = email.split("@")[0];
        String exitUserName = (String)crewMemberMap.get("USER_NAME");

        crewSettingService.deleteMember(email, crewNum);
        CrewDTO dto = crewDetailService.getCrewData(crewNum);

        log.info(crewNum + "/" + dto.getCrew_name() + " 크루에서 " + email + " 강퇴 완료");

        // 크루 알림 추가
        String crewAlertContent = "선원 " + exitUserName + "(" + exitUserEmailSplit +")님이 강퇴되었습니다.";
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
        crewAlertService.insertCrewAlert(crewAlertService.cAlertMaxNum() + 1, dto.getCrew_num(),
                "강퇴", crewAlertContent, todayStr);

        // 내 알림 추가
        String myAlertContent
                = "[" + dto.getCrew_name() + "]" + exitUserName + "(" + exitUserEmailSplit +") 선원이 강퇴되었습니다.";

        List<Map<String,String>> crewMemberList = crewSettingService.getCrewMemberList(crewNum);
        int boardNum =0;

        //윤하 크루넘 추가합니다..!
        for(int i=0;i<crewMemberList.size();i++) {
            mypageService.insertMyAlert(mypageService.maxMyalertNum()+1,crewNum,"멤버 강퇴",
                    myAlertContent, todayStr, crewMemberList.get(i).get("MEM_EMAIL"),boardNum);
        }


    }


}
