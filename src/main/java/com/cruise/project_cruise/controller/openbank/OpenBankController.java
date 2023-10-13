package com.cruise.project_cruise.controller.openbank;

import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.CrewCommentService;
import com.cruise.project_cruise.service.DevelopOpenBankingService;
import com.cruise.project_cruise.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(value="/develop/openbank")
@RestController
public class OpenBankController {

    @Autowired
    private DevelopOpenBankingService developOpenBankingService;

    @RequestMapping(value="")
    public ModelAndView bankAccountList() throws Exception {

        ModelAndView mav = new ModelAndView();

        List<OpenBankDTO> accountList =
                developOpenBankingService.getAccountList();

        mav.addObject("accountList",accountList);
        mav.setViewName("forDevelop/account");

        return mav;
    }

    @PostMapping(value="/addAccount")
    public ModelAndView bankAccountAdd(OpenBankDTO openBankDTO) throws Exception {
        ModelAndView mav = new ModelAndView();

        developOpenBankingService.insertAccount(openBankDTO);
        System.out.println("[Develop] 오픈뱅킹 테스트 계좌 insert : " + openBankDTO.getOpen_bank()+ " " +openBankDTO.getOpen_account());
        mav.setViewName("redirect: ");
        return mav;
    }

    @PostMapping(value="/updateAccount")
    public ModelAndView bankAccountUpdate(OpenBankDTO openBankDTO) throws Exception {
        ModelAndView mav = new ModelAndView();
        developOpenBankingService.updateAccount(openBankDTO);
        System.out.println("[Develop] 오픈뱅킹 테스트 계좌 Update : " + openBankDTO.getOpen_bank()+ " " +openBankDTO.getOpen_account());
        mav.setViewName("redirect: ");
        return mav;
    }

    @PostMapping(value="/deleteAccount")
    public ModelAndView bankAccountDelete(@RequestParam String account) throws Exception {
        ModelAndView mav = new ModelAndView();

        developOpenBankingService.deleteAccount(account);
        System.out.println("[Develop] 오픈뱅킹 테스트 계좌 Delete : " + account);
        mav.setViewName("redirect: ");
        return mav;
    }
}
