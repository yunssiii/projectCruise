package com.cruise.project_cruise.controller;


import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import com.cruise.project_cruise.service.MypageService;
import com.cruise.project_cruise.token.JwtTokenizer;
import com.cruise.project_cruise.util.CrewBoardUtil;

import lombok.RequiredArgsConstructor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;




@RestController
public class MypageController {

    @Autowired
    private MypageService mypageService;

    @Autowired
    JwtTokenizer jwtTokenizer;

    @Autowired
    CrewBoardUtil myUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /*
        계좌 등록 메소드

    @PostMapping("/mypage/mypage_all")
    public ModelAndView accountInsert(@RequestParam String anum) throws Exception{

        String email = "hchdbsgk@naver.com";

        System.out.println("번호 : "+ anum);

        ModelAndView mav = new ModelAndView();

        mypageService.insertAccount(email,anum);

        mav.setViewName("redirect:/mypage/mypage_all");

        return mav;
    }
     */


    /*
        계좌 내역 조회
     */
    @PostMapping("/mypage/useAccount")
    @ResponseBody
    public List<Map<String,Object>> useAccount(@RequestParam("accountNum") String accountNum,
                                   @RequestParam("months") int months) throws Exception{

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        HashMap<String,Object> hashMap = new HashMap<>();

        //System.out.println("Month : " + months);
        //System.out.println("------계좌번호 : "+accountNum);

        if(months == 1){
            List<OpenBankUsingDTO> useAccounts1 = mypageService.getUseAccounts(accountNum,months);

            for(int i=0;i<useAccounts1.size();i++){
                hashMap.put("openUseDate", useAccounts1.get(i).getOpenuse_date());
                hashMap.put("openUseContent", useAccounts1.get(i).getOpenuse_content());
                hashMap.put("openuseAssort", useAccounts1.get(i).getOpenuse_assort());
                hashMap.put("openUseIn", useAccounts1.get(i).getOpenuse_inmoney());
                hashMap.put("openUseOut", useAccounts1.get(i).getOpenuse_outmoney());

                jsonObject = new JSONObject(hashMap);
                jsonArray.add(jsonObject);
            }

            System.out.println("1달 계좌내역-> " + jsonArray);

            return jsonArray;

        }else if(months == 3){

            List<OpenBankUsingDTO> useAccounts2 = mypageService.getUseAccounts(accountNum,months);

            for(int i=0;i<useAccounts2.size();i++){
                hashMap.put("openUseDate", useAccounts2.get(i).getOpenuse_date());
                hashMap.put("openUseContent", useAccounts2.get(i).getOpenuse_content());
                hashMap.put("openuseAssort", useAccounts2.get(i).getOpenuse_assort());
                hashMap.put("openUseIn", useAccounts2.get(i).getOpenuse_inmoney());
                hashMap.put("openUseOut", useAccounts2.get(i).getOpenuse_outmoney());

                jsonObject = new JSONObject(hashMap);
                jsonArray.add(jsonObject);
            }

            System.out.println("3달 계좌내역-> " + jsonArray);

            return jsonArray;

        }else if(months == 6){


            List<OpenBankUsingDTO> useAccounts3 = mypageService.getUseAccounts(accountNum,months);

            for(int i=0;i<useAccounts3.size();i++){
                hashMap.put("openUseDate", useAccounts3.get(i).getOpenuse_date());
                hashMap.put("openUseContent", useAccounts3.get(i).getOpenuse_content());
                hashMap.put("openuseAssort", useAccounts3.get(i).getOpenuse_assort());
                hashMap.put("openUseIn", useAccounts3.get(i).getOpenuse_inmoney());
                hashMap.put("openUseOut", useAccounts3.get(i).getOpenuse_outmoney());


                jsonObject = new JSONObject(hashMap);
                jsonArray.add(jsonObject);
            }

            System.out.println("6달 계좌내역-> " + jsonArray);

            return jsonArray;
        }

        return jsonArray;

    }

    /*
        계좌명 수정
        수정 후 뿌려주기 위해 다시 조회
     */
    @PostMapping("/mypage/updateAname")
    @ResponseBody
    public List<Map<String,Object>> updateAname(HttpSession session,@RequestParam("myaccountNum") String myaccountNum,
                                                @RequestParam("myaccountName") String myaccountName) throws Exception{

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        HashMap<String,Object> hashMap = new HashMap<>();

        //세션에서 가져온 이메일
        String email = (String) session.getAttribute("email");

        System.out.println(email);

        mypageService.updateAname(myaccountName,myaccountNum); //계좌명 수정
        List<MyAccountDTO> accountLists = mypageService.getOneAccount(email,myaccountNum); //하나의 가상계좌정보

        for(int i=0;i<accountLists.size();i++){
            hashMap.put("selectAname", accountLists.get(i).getMyaccount_name());

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);

        }

        System.out.println("변경된 계좌명-> " + jsonArray);

        return jsonArray;
    }

    /*
        등록된 계좌 삭제 메소드
        - 모임통장인 경우, 참조키 제약으로 삭제되지 않음
     */
    @PostMapping("/mypage/deleteAccount")
    public void deleteMyaccount(@RequestParam("myaccountNum") String myaccountNum) throws Exception{

        mypageService.deleteMyaccount(myaccountNum);

    }

    /*
        크루 '탈퇴하기' 버튼 누를 때 get방식으로 삭제하는 메소드
     */
    @PostMapping("/mypage/mypage_all_ok")
    @ResponseBody
    public String delCrew(@RequestParam("crewNum") int crewNum,HttpServletRequest request) throws  Exception {
        //ModelAndView mav = new ModelAndView();
        String Response = "";

        //crew삭제값
        int delcrews = 0;

        System.out.println("crewNum-> " + crewNum);

        //세션에서 가져온 이메일
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");
        String capEmail = mypageService.getOneCaptain(email,crewNum);

        System.out.println("캡틴 이메일-> " + capEmail);

        if(capEmail.equals(email)){
            Response = "none";
        }else {
            //삭제
            delcrews = mypageService.deleteCrew(email,crewNum);
            if(delcrews == 1){
                Response = "OK";
            }else{
                Response = "false";
            }
        }
        return Response;

    }

    /*
        크루즈웹 비밀번호 페이지
    */
    @GetMapping("mypage/mypage_webPassword")
    public ModelAndView webPassword(HttpServletRequest request) throws Exception {

        //세션에서 가져온 이메일
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");

        String webPassword = mypageService.getWebpassword(email);
        UserDTO userInfo = mypageService.getUserInfo(email);

        ModelAndView mav = new ModelAndView();

        if (webPassword == null){
            mav.setViewName("mypage/mypage_addWebPassword");
        }else {
            mav.setViewName("mypage/mypage_changeWebPassword");
        }

        mav.addObject("userInfo",userInfo); //왼쪽 바에 이름/이메일

        return mav;
    }

    /*
        웹 비밀번호 등록/변경
     */
    @PostMapping("mypage/mypage_webPassword")
    public ModelAndView webPassword(@RequestParam String payPwd,HttpServletRequest request) throws Exception {

        //세션에서 가져온 이메일
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");

        ModelAndView mav = new ModelAndView();

        if(payPwd !=null){
            mypageService.updateWebpassword(payPwd,email);

            mav.setViewName("redirect:/mypage/mypage_all"); //등록/변경 후 마이페이지 메인으로 이동
        }

        return mav;
    }

    /*
        일정 조회
     */
    @RequestMapping("/mypage/mypage_all_sche")
    @ResponseBody
    public List<Map<String,Object>> loadMySchedule (@RequestParam("email") String email) throws Exception {

        List<ScheduleDTO> myScheLists = mypageService.getSchedule(email); //로그인한 사용자의 일정들

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        HashMap<String,Object> hashMap = new HashMap<>();

        for (int i=0;i<myScheLists.size();i++) {
            hashMap.put("title", myScheLists.get(i).getSche_title());
            hashMap.put("start", myScheLists.get(i).getSche_start());
            hashMap.put("end", myScheLists.get(i).getSche_end());
            hashMap.put("allDay", myScheLists.get(i).getSche_alldayTF());
            hashMap.put("color", myScheLists.get(i).getSche_assort());
            hashMap.put("textColor", "#FFFFFF");

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    /*
        하루 선택 시 일정 조회
    */
    @RequestMapping("/mypage/mypage_onedaySche")
    @ResponseBody
    public List<Map<String,Object>> oneDayScheLoad (HttpSession session,@RequestParam("email") String email,@RequestParam("clickDate") String clickDate) throws Exception {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        HashMap<String,Object> hashMap = new HashMap<>();

        List<ScheduleDTO> onedayScheLists = mypageService.getOneSchedule(email,clickDate);

        System.out.println("하루 일정 조회>>>>>>>>>" + onedayScheLists);

        for(int i=0;i<onedayScheLists.size();i++){
            int getCrewName = onedayScheLists.get(i).getCrew_num();

            System.out.println("하루 일정 크루번호 >>>>>>>>>" + getCrewName);

            String crewName = mypageService.getScheCrewName(email,getCrewName);

            onedayScheLists.get(i).setCrew_name(crewName);

            System.out.println("하루 일정 조회2222 >>>>>>>>>" + onedayScheLists);

            hashMap.put("title", onedayScheLists.get(i).getSche_title());
            hashMap.put("start", onedayScheLists.get(i).getSche_start());
            hashMap.put("end", onedayScheLists.get(i).getSche_end());
            hashMap.put("allDay", onedayScheLists.get(i).getSche_alldayTF());
            hashMap.put("color", onedayScheLists.get(i).getSche_assort());
            hashMap.put("crewName", onedayScheLists.get(i).getCrew_name());

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);

        }

        System.out.println("JSON형태의 하루 일정 >>>>>>>>>" + jsonArray);

        return jsonArray;

    }


    /*
        내 정보 수정 페이지 - get방식, 보여주기
     */
    @GetMapping("mypage/mypage_myInfo")
    public ModelAndView myInfo(HttpServletRequest request) throws Exception {

        //세션에서 가져온 이메일
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");

        UserDTO userInfo = mypageService.getUserInfo(email);

        ModelAndView mav = new ModelAndView();

        mav.addObject("userInfo",userInfo); //왼쪽 바에 이름/이메일

        mav.setViewName("mypage/mypage_myInfo");

        return mav;

    }

    /*
        내 정보 수정 페이지 - post방식, 찐 수정
     */
    @PostMapping("mypage/mypage_myInfo")
    public ModelAndView myInfo(HttpSession session,@RequestParam("tel") String tel,
                       @RequestParam("address")String address,@RequestParam("detailAddress")String detailAddress) throws Exception {

        //세션에서 가져온 이메일
        String email = (String) session.getAttribute("email");

        mypageService.updateUserInfo(tel, address, detailAddress, email);

        ModelAndView mav = new ModelAndView();

        mav.setViewName("redirect:/mypage/mypage_myInfo");

        System.out.println(">>>>> 사용자 정보 수정 완료");

        return mav;

    }

    /*
        내 정보 수정 페이지 - post방식, 비밀번호만 수정
     */
    @PostMapping("mypage/mypage_myInfo_pwd")
    public ModelAndView myPWd(HttpServletResponse response,HttpSession session,@RequestParam("newPwd") String newPwd,@RequestParam("chkNewPwd") String chkNewPwd) throws Exception {

        //세션에서 가져온 이메일
        String email = (String) session.getAttribute("email");

        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String rawPassword = newPwd; 
        String encPassword = bCryptPasswordEncoder.encode(rawPassword); //해싱한 비밀번호

        //같으면 수정
        if(newPwd != null && chkNewPwd != null && newPwd.equals(chkNewPwd)){
            mypageService.updateUserPwd(encPassword,email);
            System.out.println(">>>>>> 사용자 비밀번호 수정 완료");
        }

        ModelAndView mav = new ModelAndView();

        mav.setViewName("redirect:/mypage/mypage_myInfo");

        return mav;

    }

    /*
        내 게시글 페이지
     */
    @GetMapping("mypage/mypage_board")
    public ModelAndView myBoard(HttpServletRequest request) throws Exception {

        //세션에서 가져온 이메일
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");

        UserDTO userInfo = mypageService.getUserInfo(email);
        List<CrewCommentDTO> myCommentLists = mypageService.getMyComment(email); //내 댓글 조회

        String pageNum = request.getParameter("pageNum");

        int currentPage = 1;

        if(pageNum != null) {
            currentPage = Integer.parseInt(pageNum);
        }

        int boardCount = mypageService.getBoardCount(email);

        int numPerPage = 5; //한 페이지 표시될 게시글 수
        int totalPage = myUtil.getPageCount(numPerPage,boardCount);

        if(currentPage > totalPage){
            currentPage = totalPage;
        }

        int start = (currentPage -1) * numPerPage + 1;
        int end = currentPage * numPerPage;

        List<CrewBoardDTO> myBoardLists = mypageService.getMyboard(email,start,end); //내 게시글 조회

        //게시글 크루명 조회 후 myBoardLists 넣기
        for (CrewBoardDTO dto : myBoardLists){
            String boardStr = mypageService.getCrewName(dto.getCrew_num());
            dto.setCrew_name(boardStr);
        }

        for (CrewCommentDTO dto : myCommentLists){
            String commentStr = mypageService.getCrewName(dto.getCrew_num());
            String commentSubject = mypageService.getBoardSubject(dto.getBoard_num());

            dto.setCrew_name(commentStr);
            dto.setBoard_subject(commentSubject);
        }

        String boardUrl = "/mypage/mypage_board";

        String boardIndexList = myUtil.pageIndexList(currentPage,totalPage,boardUrl);

        ModelAndView mav = new ModelAndView();

        mav.addObject("userInfo",userInfo); //왼쪽 바에 이름/이메일
        mav.addObject("myBoardLists",myBoardLists); //내 게시글들
        mav.addObject("myCommentLists",myCommentLists); //내 댓글들

        mav.addObject("boardIndexList",boardIndexList);
        mav.addObject("boardCount",boardCount);
        mav.addObject("boardUrl",boardUrl);
        mav.addObject("pageNum",currentPage);

        mav.setViewName("mypage/mypage_board");

        return mav;

    }

    /*
        내 게시글 삭제
     */
    @PostMapping("mypage/mypage_board_board")
    public void myBoardDel(@RequestParam(value = "chkBoardLists[]") List<String> chkBoardLists) throws Exception {

        //String email = "hchdbsgk@naver.com";

        //게시글 삭제
        for (String str : chkBoardLists){
            int chkBoards = Integer.parseInt(str);

            System.out.println("------이거야2 : "+chkBoards);

            mypageService.deleteMyboard(chkBoards);

        }
    }

    @PostMapping("mypage/mypage_board_comment")
    public void myCommentDel(@RequestParam(value = "chkCommentLists[]") List<String> chkCommentLists) throws Exception {

        //String email = "hchdbsgk@naver.com";

        //게시글 삭제
        for (String str : chkCommentLists){
            int chkComments = Integer.parseInt(str);

            System.out.println("------이거야댓 : "+chkComments);

            mypageService.deleteMycomment(chkComments);

        }
    }


    /*
        내 알림 페이지
     */
    @GetMapping("mypage/mypage_myAlert")
    public ModelAndView myAlert(HttpServletRequest request) throws Exception {

        //세션에서 가져온 이메일
        HttpSession session = request.getSession();
        String email = (String)session.getAttribute("email");

        UserDTO userInfo = mypageService.getUserInfo(email);
        List<MyAlertDTO> myAlertList = mypageService.getMyalert(email);
        //List<CrewBoardDTO> myBoardLists = mypageService.getMyboardLink(email); //내 게시글 조회

        //보드넘, 크루이름 피료..

        //크루 넘으로 크루네임 가져와서 myAlertList에 넣기
        for (int i=0;i<myAlertList.size();i++) {
            String crewName = mypageService.getCrewName(myAlertList.get(i).getCrew_num());
            myAlertList.get(i).setCrew_name(crewName);
        }

        ModelAndView mav = new ModelAndView();

        mav.addObject("userInfo",userInfo); //왼쪽 바에 이름/이메일
        mav.addObject("myAlertList",myAlertList);

        //System.out.println("보드넘 >>>>" + myAlertList.get(0).getBoard_num());

        mav.setViewName("mypage/mypage_alert");

        return mav;

    }

    /*
        알림 삭제 메소드
     */
    @PostMapping("mypage/mypage_myAlert")
    public void delMyAlert(@RequestParam(value = "chkAlertLists[]") List<String> chkAlertLists) throws Exception {

        //String email = "hchdbsgk@naver.com";

        //게시글 삭제
        for (String str : chkAlertLists){
            int chkMyalert = Integer.parseInt(str);

            System.out.println("삭제할 게시글 >>>>>>>>"+chkMyalert);

            mypageService.deleteMyalert(chkMyalert);

        }
    }

    /*
        회원 탈퇴 메소드
        - 모임장인 경우, 참조키 제약으로 삭제되지 않음
     */
    @PostMapping("/mypage/deleteUser")
    public void deleteUser(@RequestParam("email") String email) throws Exception{

        System.out.println("탈퇴할 이메일 >>>>>"+email);

        mypageService.deleteUser(email);

    }


    /*
        nav 알림 데이터
    */
    @PostMapping("/nav/alert")
    @ResponseBody
    public List<Map<String,Object>> navAlertSelect(HttpSession session) throws Exception{

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        HashMap<String,Object> hashMap = new HashMap<>();

        //세션에서 가져온 이메일
        String email = (String) session.getAttribute("email");

        //System.out.println(email);

        List<MyAlertDTO> navAlertList = mypageService.getNavAlert(email);
        //List<CrewBoardDTO> myBoardLists = mypageService.getMyboardLink(email); //내 게시글 조회

//        //게시글 크루명 조회 후 myBoardLists 넣기
//        for (CrewBoardDTO dto : myBoardLists){
//            String boardStr = mypageService.getCrewName(dto.getCrew_num());
//            dto.setCrew_name(boardStr);
//        }

        for (int i=0;i<navAlertList.size();i++) {
            String crewName = mypageService.getCrewName(navAlertList.get(i).getCrew_num());
            navAlertList.get(i).setCrew_name(crewName);
        }

        for(int i=0;i<navAlertList.size();i++){
            hashMap.put("alertContent", navAlertList.get(i).getMyalert_content());
            hashMap.put("alertAssort", navAlertList.get(i).getMyalert_assort());
            hashMap.put("alertDate", navAlertList.get(i).getMyalert_adate());
            hashMap.put("crewNum", navAlertList.get(i).getCrew_num());
            hashMap.put("boardNum", navAlertList.get(i).getBoard_num());
            hashMap.put("crewName", navAlertList.get(i).getCrew_name());
//            hashMap.put("boardNum", myBoardLists.get(i).getBoard_num());
//            hashMap.put("crewName", myBoardLists.get(i).getCrew_name());

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);

        }

        System.out.println("조회될 네비바 알림 >>>>>>>>> " + jsonArray);

        return jsonArray;
    }


}
