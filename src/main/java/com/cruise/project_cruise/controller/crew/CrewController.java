package com.cruise.project_cruise.controller.crew;

import com.cruise.project_cruise.service.CrewDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView crewMain() throws Exception {
        ModelAndView mav = new ModelAndView();

        // FIXME Post 방식으로 CrewNum을 넘겨받기

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
