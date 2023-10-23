package com.cruise.project_cruise.controller.moim;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.MoimPassbookService;
import com.cruise.project_cruise.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
@RequestMapping("/moim/passbook")
@RestController
public class PassbookController {

    @Autowired
    private MoimPassbookService moimPassbookService;
    @Autowired
    private CrewBoardService crewBoardService;
    @Autowired
    private MypageService mypageService;

    @GetMapping(value = "")
    public ModelAndView passbook(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
//        String userEmail = (String)session.getAttribute("email");
        String userEmail = "dlaldus@naver.com";
        String userName = crewBoardService.getUserName(userEmail);

        ModelAndView mav = new ModelAndView();

        List<MyAccountDTO> myAccount = moimPassbookService.getMyAccount(userEmail);

        // 등록된 계좌가 없는 경우 th:if로 '기존 계좌 선택' 영역 안 보이게 하기
        if(myAccount.isEmpty()) {
            mav.addObject("account", 0);
        }

        mav.addObject("myAccount", myAccount);  // 기존 계좌 불러오기
        mav.addObject("userName", userName);    // 모달3에 '이름' 전달

        mav.setViewName("moim/passbook");

        return mav;
    }

    @PostMapping(value = "")
    public ModelAndView passbook_ok(@RequestParam("checkedBox") String checkedBox,
                                    CrewDTO crewDTO, CrewMemberDTO crewMemberDTO,
                                      HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
//        String userEmail = (String)session.getAttribute("email");
        String userEmail = "dlaldus@naver.com";

        ModelAndView mav = new ModelAndView();
        // 새로운 계좌 추가한 경우---------------------------------------------
        System.out.println("checkedBox : " + checkedBox);
        System.out.println("crewDTO.getCrew_accountid() : " + crewDTO.getCrew_accountid());
        if (checkedBox.equals("checkedNew")) {
            String bankName = moimPassbookService.getBankName(crewDTO.getCrew_accountid());
            crewDTO.setCrew_bank(bankName); // 계좌명
            crewDTO.setCrew_accountid(crewDTO.getCrew_accountid());   // 계좌번호
        } else {    // 기존 계좌 선택한 경우-----------------------------------
            String selectedAccount = request.getParameter("my_account");    // select box에서 선택한 '은행명 계좌번호'
            String[] parts = selectedAccount.split(" ");    // 띄어쓰기를 기준으로 나눠서 따로 insert
            if (parts.length == 2) {
                String bankName = parts[0]; // 계좌명
                String accountNumber = parts[1]; // 계좌번호

                crewDTO.setCrew_bank(bankName);
                crewDTO.setCrew_accountid(accountNumber);
            }
        }

        // 목표 금액(선택): CrewDTO에서 기본값 0으로 설정
        crewDTO.setCrew_goal(crewDTO.getCrew_goal());

        int maxCrewNum = moimPassbookService.maxCrewNum() + 1;

        crewDTO.setCrew_num(maxCrewNum);
        crewDTO.setCrew_name(crewDTO.getCrew_name());

        // 모임 소개(선택)
        if(crewDTO.getCrew_info() != null && !crewDTO.getCrew_info().isEmpty()) {
            crewDTO.setCrew_info(crewDTO.getCrew_info());
        } else {
            crewDTO.setCrew_info(crewDTO.getCrew_name() + " 크루 입니다.");
        }
        crewDTO.setCaptain_email(userEmail);
        crewDTO.setCrew_paymoney(crewDTO.getCrew_paymoney());
        crewDTO.setCrew_paydate(crewDTO.getCrew_paydate());

        moimPassbookService.insertCrew(crewDTO);

        crewMemberDTO.setCrew_num(maxCrewNum);
        crewMemberDTO.setCmem_email(userEmail);

        moimPassbookService.insertCrewMember(crewMemberDTO);


        mav.setViewName("redirect:/mypage/mypage_all");

        return mav;
    }

    @PostMapping("/new")
    public String new_account(MyAccountDTO myAccountDTO, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
//        String userEmail = (String)session.getAttribute("email");
        String userEmail = "dlaldus@naver.com";

//        ModelAndView mav = new ModelAndView();

//        List<OpenBankDTO> openAccPwd = mypageService.getOpenAccPWd("hchdbsgk@naver.com"); //가상계좌 비밀번호
//        if(openAccPwd != null){
//            mav.addObject("openAccPwd",openAccPwd);
//        }
        mypageService.insertAccount(userEmail,myAccountDTO.getMyaccount_anum());

        return "insertNewAccount";
    }
}
