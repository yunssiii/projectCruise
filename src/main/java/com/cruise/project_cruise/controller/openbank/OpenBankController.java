package com.cruise.project_cruise.controller.openbank;

import com.cruise.project_cruise.service.TemplateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RequestMapping(value="/develop/openbank")
@RestController
public class OpenBankController {

    @Resource
    private TemplateService templateService;

    @RequestMapping(value="/list")
    public ModelAndView bankAccountList() throws Exception {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("template");

        return mav;
    }

}
