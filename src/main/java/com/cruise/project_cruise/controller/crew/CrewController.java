package com.cruise.project_cruise.controller.crew;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.ScheduleDTO;
import com.cruise.project_cruise.service.CrewDetailService;
import com.cruise.project_cruise.service.CrewSettingService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value="/crew")
@RestController
public class CrewController {

    // 스케줄 데이터를 전달해주는 URL - main, setting, mypage에서 공동 사용
    @RequestMapping("/setting/loadCrewSchedule")
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
            calHash.put("end", crewScheList.get(i).getSche_end());
            if(crewScheList.get(i).getSche_alldayTF().equals("true")){
                allDay = true;
            } else {
                allDay = false;
            }
            calHash.put("allDay", allDay);
            calHash.put("color", crewScheList.get(i).getSche_assort());
            calHash.put("textColor", "#FFFFFF");

            jsonObject = new JSONObject(calHash);
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    /**
     * TODO [CrewController] : 만들어야 할 메소드 목록
     * 1. 메인화면 출력
     * 2. 크루관리페이지 출력
     */

    @Autowired
    private CrewDetailService crewDetailService;
    @Autowired
    private CrewSettingService crewSettingService;

    @RequestMapping(value="")
    public ModelAndView crewMain(HttpServletRequest request) throws Exception {
        // 크루 상세페이지 메인화면
        ModelAndView mav = new ModelAndView();
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

        // TODO 0. 크루원만 해당크루 상세페이지에 접속가능하게 처리하기
            // 1. 세션에서 크루원의 이메일 받기
            // 2. 선원 테이블에서 이메일이 해당 이메일이고 crewnum이 해당 num인 데이터 찾기
            // 3. 존재하면 접속가능, 존재하지 않으면 return 오류페이지

        // TODO 1. 크루 소식조회

        // 2. 크루 기본정보 데이터 - 완료
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
            int achievePer = (int)(((double)crewAccountBalance/(double)dto.getCrew_goal())*100);
                // int 는 정수이기 때문에 나눗셈의 소숫점 결과값을 얻으려면 double로 형변환 해주어야 함.
            String crewGoal = decimalFormat.format(dto.getCrew_goal());


        // TODO 거래내역 조회

        // TODO 회비내역 조회

        // TODO 납입기능 양식 출력

        // TODO 일정 간편조회

        // 데이터 넘겨주기
        mav.addObject("dto",dto);
        mav.addObject("crewNum",crewNum);
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
    public ModelAndView crewSetting(HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView();
        int crewNum = Integer.parseInt(request.getParameter("crewNum"));
        CrewDTO dto = crewDetailService.getCrewData(crewNum);

        mav.addObject("dto",dto);
        mav.addObject("crewNum",crewNum);
        mav.setViewName("crew/crewSetting");

        return mav;
    }

    @RequestMapping(value="/setting/addCrewSche")
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

        ScheduleDTO dto = new ScheduleDTO();
        dto.setSche_num(crewSettingService.getScheMaxNum()+1);
        dto.setCrew_num(crewNum);
        dto.setSche_title(scheTitle);
        dto.setSche_assort(scheAssortCode);
        dto.setSche_alldayTF(scheAllDayTF);
        dto.setSche_start(scheStart);
        dto.setSche_end(scheEnd);

        crewSettingService.insertCrewSche(dto);
        System.out.println("[CrewController - Setting : Calendar] 일정 추가완료 - crewNum " + crewNum + " scheTitle " + scheTitle);

    }

    @RequestMapping(value="/setting/updateCrewSche")
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
        System.out.println("[CrewController - Setting : Calendar] 일정 수정완료 - scheNum " + scheNum + " scheTitle " + scheTitle);

    }

    @RequestMapping(value="/setting/deleteCrewSche")
    @ResponseBody
    public void deleteCrewSche(@RequestParam("scheNum") int scheNum) throws Exception {
        crewSettingService.deleteCrewSche(scheNum);
        System.out.println("[CrewController - Setting : Calendar] 일정 삭제완료 - scheNum " + scheNum);
    }

}
