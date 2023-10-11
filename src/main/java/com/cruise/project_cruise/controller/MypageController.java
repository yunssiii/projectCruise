package com.cruise.project_cruise.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MypageController {


    @GetMapping("/mypage_zero")
    public ModelAndView zero() throws Exception {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("mypage_zero");

        return mav;

    }


}
