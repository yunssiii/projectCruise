package com.cruise.project_cruise.controller.moim;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.MoimPassbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class PassbookController {

    @Autowired
    private MoimPassbookService moimPassbookService;
    @Autowired
    private CrewBoardService crewBoardService;

    @GetMapping("/moim/passbook")
    public ModelAndView passbook(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String userEmail = (String)session.getAttribute("email");

        crewBoardService.getUserName(userEmail);

        List<MyAccountDTO> myAccount = moimPassbookService.getMyAccount(userEmail);

        ModelAndView mav = new ModelAndView();

        mav.addObject("myAccount", myAccount);

        mav.setViewName("moim/passbook");

        return mav;
    }

    @PostMapping("/moim/passbook")
    public ModelAndView crew_passbook(CrewDTO crewDTO, CrewMemberDTO crewMemberDTO,
                                      HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String userEmail = (String)session.getAttribute("email");

        String selectedAccount = request.getParameter("my_account");
        String[] parts = selectedAccount.split(" ");
        if (parts.length == 2) {
            String bankName = parts[0]; // 계좌명
            String accountNumber = parts[1]; // 계좌번호

            crewDTO.setCrew_bank(bankName);
            crewDTO.setCrew_accountid(accountNumber);
        }

        String crewName = request.getParameter("crewName");
        String crewInfo = request.getParameter("crewInfo");
        try {   // 목표 금액(선택)
            int crewGoal = Integer.parseInt(request.getParameter("crewGoal"));
            crewDTO.setCrew_goal(crewGoal);
        } catch (NumberFormatException e) {
            int crewGoal = 0; // 기본값 설정
            crewDTO.setCrew_goal(crewGoal);
        }
        int crewPaymoney = Integer.parseInt(request.getParameter("crewPaymoney"));
        int crewPaydate = Integer.parseInt(request.getParameter("crewPaydate"));

        int maxCrewNum = moimPassbookService.maxCrewNum() + 1;

        crewDTO.setCrew_num(maxCrewNum);
        crewDTO.setCrew_name(crewName);
        // 모임 소개(선택)
        if(crewInfo != null && !crewInfo.isEmpty()) {
            crewDTO.setCrew_info(crewInfo);
        } else {
            crewDTO.setCrew_info(crewName + " 크루 입니다.");
        }
        crewDTO.setCaptain_email(userEmail);
        crewDTO.setCrew_paymoney(crewPaymoney);
        crewDTO.setCrew_paydate(crewPaydate);

        moimPassbookService.insertCrew(crewDTO);

        crewMemberDTO.setCrew_num(maxCrewNum);
        crewMemberDTO.setCmem_email(userEmail);
        crewMemberDTO.setCaptain_YN("Y");

        moimPassbookService.insertCrewMember(crewMemberDTO);

        ModelAndView mav = new ModelAndView();

        mav.setViewName("redirect:/mypage/mypage_all");

        return mav;

    }
}
