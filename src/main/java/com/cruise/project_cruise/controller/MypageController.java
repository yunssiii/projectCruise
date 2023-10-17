package com.cruise.project_cruise.controller;


import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.service.MypageService;
import com.cruise.project_cruise.util.CrewBoardUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MypageController {

    @Autowired
    private MypageService mypageService;

    @Autowired
    CrewBoardUtil myUtil;


    /*
        로그인 후 바로 연결되는 마이페이지 메인창 메소드
        크루와 계좌 0이면 zero페이지 보여지고
        0 이상이면 all페이지 보여짐
        --
        일정 달력 조회
     */
    @GetMapping("/mypage/mypage_all")
    public ModelAndView all(HttpServletRequest request) throws  Exception {

        String email = "hchdbsgk@naver.com";

        List<CrewDTO> crewLists = mypageService.getCrews(email); //크루 정보
        List<CrewMemberDTO> crewNumLists = mypageService.getCrewNums(email); //크루맴버의 크루번호
        List<OpenBankDTO> openAccPwd = mypageService.getOpenAccPWd(email); //가상계좌 비밀번호
        List<OpenBankDTO> accountLists = mypageService.getAccounts(email); //가상계좌정보
        UserDTO userInfo = mypageService.getUserInfo(email); // 로그인한 사용자 정보.이름


        ModelAndView mav = new ModelAndView();

        if(!crewNumLists.isEmpty() || !accountLists.isEmpty()){
            mav.setViewName("mypage/mypage_all");

            mav.addObject("crewLists",crewLists);
            mav.addObject("userInfo",userInfo);

            if(openAccPwd != null){
                mav.addObject("openAccPwd",openAccPwd);
            }

            mav.addObject("accountLists",accountLists);

        }else {
            mav.setViewName("mypage/mypageZero");
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

        System.out.println("이거-> " + myScheLists);

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

        System.out.println("이거2-> " + jsonArray);

        return jsonArray;
    }

    /*
        계좌 등록 메소드
     */
    @PostMapping("/mypage/mypage_all")
    public ModelAndView accountInsert(@RequestParam String anum) throws Exception{

        String email = "hchdbsgk@naver.com";

        System.out.println("번호 : "+ anum);

        ModelAndView mav = new ModelAndView();

        mypageService.insertAccount(email,anum);

        mav.setViewName("redirect:/mypage/mypage_all");

        return mav;
    }

    /*
        크루 '탈퇴하기' 버튼 누를 때 get방식으로 삭제하는 메소드
     */
    @GetMapping("/mypage/mypage_all_ok")
    public ModelAndView delCrew(HttpServletRequest request) throws  Exception {

        String email = "hchdbsgk@naver.com";
        int crewNum = Integer.parseInt(request.getParameter("crewNum"));

        mypageService.deleteCrew(email,crewNum);

        ModelAndView mav = new ModelAndView();

        mav.setViewName("redirect:/mypage/mypage_all");

        return mav;

    }

    /*
        크루즈웹 비밀번호 페이지
    */
    @GetMapping("mypage/mypage_webPassword")
    public ModelAndView webPassword() throws Exception {

        String email = "hchdbsgk@naver.com";

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
    public ModelAndView webPassword(@RequestParam String payPwd) throws Exception {

        String email = "hchdbsgk@naver.com";

        ModelAndView mav = new ModelAndView();

        if(payPwd !=null){
            mypageService.updateWebpassword(payPwd,email);

            mav.setViewName("redirect:/mypage/mypage_all"); //등록/변경 후 마이페이지 메인으로 이동
        }

        return mav;
    }

    /*
        내 정보 수정 페이지
     */
    @GetMapping("mypage/mypage_myInfo")
    public ModelAndView myInfo() throws Exception {

        String email = "hchdbsgk@naver.com";

        UserDTO userInfo = mypageService.getUserInfo(email);

        ModelAndView mav = new ModelAndView();

        mav.addObject("userInfo",userInfo); //왼쪽 바에 이름/이메일

        mav.setViewName("mypage/mypage_myInfo");

        return mav;

    }

    /*
        내 게시글 페이지
     */
    @GetMapping("mypage/mypage_board")
    public ModelAndView myBoard(HttpServletRequest request) throws Exception {

        String email = "hchdbsgk@naver.com";

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
    public ModelAndView myAlert() throws Exception {

        String email = "hchdbsgk@naver.com";

        UserDTO userInfo = mypageService.getUserInfo(email);

        ModelAndView mav = new ModelAndView();

        mav.addObject("userInfo",userInfo); //왼쪽 바에 이름/이메일

        mav.setViewName("mypage/mypage_alert");

        return mav;

    }




}
