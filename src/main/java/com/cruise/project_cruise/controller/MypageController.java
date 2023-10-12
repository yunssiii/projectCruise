package com.cruise.project_cruise.controller;


import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
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

        ModelAndView mav = new ModelAndView();

        if(!crewNumLists.isEmpty()){
            mav.setViewName("mypage/mypage_all");

            mav.addObject("crewLists",crewLists);

            if(openAccPwd != null){
                mav.addObject("openAccPwd",openAccPwd);
            }
        }else {
            mav.setViewName("mypage/mypageZero");
        }
        return mav;
    }

    /*
        계좌 등록 메소드
     */
    @PostMapping("/mypage/mypage_all")
    public ModelAndView accountInsert(HttpServletRequest request) throws Exception{

        String email = "hchdbsgk@naver.com";
        String myaccountAnum = "12341234123412";


        ModelAndView mav = new ModelAndView();

        mypageService.insertAccount(email,myaccountAnum);

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

}
