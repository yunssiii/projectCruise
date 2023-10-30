package com.cruise.project_cruise.controller;

import com.cruise.project_cruise.dto.CrewAlertDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.oauth.config.PrincipalDetails;
import com.cruise.project_cruise.oauth.provider.KakaoUserInfo;
import com.cruise.project_cruise.service.*;
import com.cruise.project_cruise.token.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.Console;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class HomeController {

    @Autowired
    UserService userService;
    @Autowired
    CrewMemberInviteService crewMemberInviteService;
    @Autowired
    CrewAlertService crewAlertService;
    @Autowired
    CrewDetailService crewDetailService;
    @Autowired
    private MypageService mypageService;
    @Autowired
    private CrewSettingService crewSettingService;





    @RequestMapping(value = "/",method = {RequestMethod.GET, RequestMethod.POST})
    public String home(HttpSession session, Model model, @RequestParam(name = "group", required = false) String group,
                       @RequestParam(name = "num", required = false) String num) throws Exception {
        //이것 때문에 엄청고생했는데 결국 OAuth2User 에서는 소셜로그인 정보만 담기는거였음
        //근데 일반로그인해서 콘솔에 principal찍어봐도 값은 나오는데 왜 Null이라고 인식하는지...
        //더 자세한 공부가 필요함..
         final Logger logger = LoggerFactory.getLogger(HomeController.class);
            logger.info("처음화면");

        if (group != null && num != null) { // group과 num 값이 파라미터에 있을 때만 세션에 값을 설정


            session.setAttribute("group", group);
            session.setAttribute("num", num);

        }

            if(session.getAttribute("email") != null){



                return "redirect:/mypage/mypage_all";

            }






        return "main";
    }






        @GetMapping("/accept")
        public String accept(HttpSession session) throws Exception {

            CrewMemberDTO dto = new CrewMemberDTO();

            String email = (String) session.getAttribute("email");
            String crew_numStr = (String) session.getAttribute("num");
            int crew_num = Integer.parseInt(crew_numStr);

            // 은지 - dto에 cmem_num 세팅하는 부분 추가
            int maxNum = crewMemberInviteService.getCmemNumMaxNum();
            dto.setCmem_num(maxNum+1);
            
            dto.setCrew_num(crew_num);
            dto.setCmem_email(email);

            crewMemberInviteService.insertCrewMember(dto);

            // 은지 - 새 멤버 가입 시 crewAlert 추가
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
            Map<String,Object> userMap = crewDetailService.getCrewUserInfo(crew_num,email);
            String userEmailSplit = email.split("@")[0];
            String crewAlertContent
                    = (String)userMap.get("USER_NAME") + "(" + userEmailSplit + ") 님이 가입하셨습니다.";

            crewAlertService.insertCrewAlert(crewAlertService.cAlertMaxNum() + 1, dto.getCrew_num(),
                    "가입", crewAlertContent, todayStr);

            // 윤하 - 새 맴버 가입 시 my_alert에 추가

            //CrewAlertDTO alertDTO = new CrewAlertDTO();
            List<Map<String, String>> crewMember = crewSettingService.getCrewMemberList(dto.getCrew_num());

            String crewName = mypageService.getCrewName(dto.getCrew_num());

            String content = "[" + crewName + "]" + " 새 맴버가 가입했습니다.";
            int boardNum =0;

            //크루 맴버 수 만큼 my_alert에 insert
            for (Map<String, String> stringStringMap : crewMember) {
                int alertNum = mypageService.maxMyalertNum() + 1;

                mypageService.insertMyAlert(alertNum, dto.getCrew_num(),"가입",
                        content, todayStr, stringStringMap.get("MEM_EMAIL"),boardNum);
            }

            session.removeAttribute("group");
            session.removeAttribute("num");


            return "redirect:/mypage/mypage_all";
        }

    @GetMapping("/accept2") //로그인시 초대
    public String accept2(HttpSession session) throws Exception {

        CrewMemberDTO dto = new CrewMemberDTO();

        String email = (String) session.getAttribute("email");
        String crew_numStr = (String) session.getAttribute("num");
        int crew_num = Integer.parseInt(crew_numStr);

        // 은지 - dto에 cmem_num 세팅하는 부분 추가
        int maxNum = crewMemberInviteService.getCmemNumMaxNum();
        dto.setCmem_num(maxNum+1);
        dto.setCrew_num(crew_num);
        dto.setCmem_email(email);

        crewMemberInviteService.insertCrewMember(dto);

        // 은지 - 새 멤버 가입 시 crewAlert 추가
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
        Map<String,Object> userMap = crewDetailService.getCrewUserInfo(crew_num,email);
        String userEmailSplit = email.split("@")[0];
        String crewAlertContent
                = (String)userMap.get("USER_NAME") + "(" + userEmailSplit + ") 님이 가입하셨습니다.";

        crewAlertService.insertCrewAlert(crewAlertService.cAlertMaxNum() + 1, dto.getCrew_num(),
                "가입", crewAlertContent, todayStr);

        // 윤하 - 새 맴버 가입 시 my_alert에 추가

        //CrewAlertDTO alertDTO = new CrewAlertDTO();
        List<Map<String, String>> crewMember = crewSettingService.getCrewMemberList(dto.getCrew_num());

        String crewName = mypageService.getCrewName(dto.getCrew_num());

        String content = "[" + crewName + "]" + " 새 맴버가 가입했습니다.";
        int boardNum =0;

        //크루 맴버 수 만큼 my_alert에 insert
        for (Map<String, String> stringStringMap : crewMember) {
            int alertNum = mypageService.maxMyalertNum() + 1;

            mypageService.insertMyAlert(alertNum, dto.getCrew_num(),"가입",
                    content, todayStr, stringStringMap.get("MEM_EMAIL"),boardNum);
        }

        session.removeAttribute("group");
        session.removeAttribute("num");


        return "redirect:/mypage/mypage_all";
    }

        @GetMapping("/reject")
        public String reject(HttpSession session){
            session.removeAttribute("group");
            session.removeAttribute("num");


            return "redirect:/mypage/mypage_all";
        }

        @GetMapping("/reject2")
        @ResponseBody
        public void reject2(HttpSession session){
            session.removeAttribute("group");
            session.removeAttribute("num");
        }

        @GetMapping("/showModalWithAjax")
        @ResponseBody
        public String showModalWithAjax(@RequestParam("email") String email, @RequestParam("num") int num) throws Exception {

            CrewMemberDTO dto = new CrewMemberDTO();
            dto.setCrew_num(num);
            dto.setCmem_email(email);
            String captainYN = crewMemberInviteService.selectCaptain(dto);



            if(captainYN == null){
                captainYN="X";
            }

            System.out.println(captainYN);
            System.out.println(email);

            if(captainYN.equals("Y") || captainYN.equals("N")){//대장이거나 선원이면 이미 모임에있는것이니 모달창 띄어줄 필요가 없음 근데 대장이나 선원이 아니면 captain값은 null임
                return "error";
            }else{
                return "success";
            }

        }


    }
