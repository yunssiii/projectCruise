package com.cruise.project_cruise.controller;


import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.UserDTO;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MypageController {

    @Autowired
    private MypageService mypageService;


    /*
        로그인 후 바로 연결되는 마이페이지 메인창 메소드
        크루가 0이면 zero페이지 보여지고
        0 이상이면 all페이지 보여짐 
     */
    @GetMapping("/mypage/mypage_all")
    public ModelAndView all(HttpServletRequest request) throws  Exception {

        String email = "hchdbsgk@naver.com";

        List<CrewDTO> crewLists = mypageService.getCrews(email); //크루 정보
        List<CrewMemberDTO> crewNumLists = mypageService.getCrewNums(email); //크루맴버의 크루번호
        List<OpenBankDTO> openAccPwd = mypageService.getOpenAccPWd(email); //가상계좌 비밀번호
        List<OpenBankDTO> accountLists = mypageService.getAccounts(email); //가상계좌정보
        UserDTO userName = mypageService.getUserName(email);

        ModelAndView mav = new ModelAndView();

        if(!crewNumLists.isEmpty()){
            mav.setViewName("mypage/mypage_all");

            mav.addObject("crewLists",crewLists);
            mav.addObject("userName",userName);

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
        '탈퇴하기' 버튼 누를 때 get방식으로 삭제하는 메소드
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

        ModelAndView mav = new ModelAndView();

        if (webPassword == null){
            mav.setViewName("mypage/mypage_addWebPassword");
        }else {
            mav.setViewName("mypage/mypage_changeWebPassword");
        }

        return mav;
    }

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

    @GetMapping("mypage/mypage_myInfo")
    public ModelAndView myInfo() throws Exception {

        String email = "hchdbsgk@naver.com";

        UserDTO userInfo = mypageService.getUserInfo(email);

        ModelAndView mav = new ModelAndView();

        mav.addObject("userInfo",userInfo);

        mav.setViewName("mypage/mypage_myInfo");

        return mav;

    }

    @GetMapping("mypage/mypage_board")
    public ModelAndView myBoard() throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("mypage/mypage_board");

        return mav;

    }


}
