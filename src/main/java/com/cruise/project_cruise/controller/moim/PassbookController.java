package com.cruise.project_cruise.controller.moim;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.service.CrewBoardService;
import com.cruise.project_cruise.service.MoimPassbookService;
import com.cruise.project_cruise.service.MypageService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String userEmail = (String)session.getAttribute("email");

        ModelAndView mav = new ModelAndView();

        if(userEmail == null) {
            mav.setViewName("redirect:/");
            return mav;
        }

        String userName = crewBoardService.getUserName(userEmail);

        List<MyAccountDTO> myAccount = moimPassbookService.getMyAccount(userEmail); // 기존 계좌 불러오기
        List<OpenBankDTO> openAccPwd = mypageService.getOpenAccPWd(userEmail); // 가상계좌 비밀번호
        List<MyAccountDTO> myaccountList = mypageService.getAccountList(userEmail); //등록된 계좌 정보 조회

        // 등록된 계좌가 없는 경우 th:if로 '기존 계좌 선택' 영역 안 보이게 하기
        if(myAccount.isEmpty()) {
            mav.addObject("account", 0);
        }

        // 계좌 비밀번호가 있으면
        if(openAccPwd != null){
            mav.addObject("openAccPwd",openAccPwd);
            mav.addObject("myaccountList",myaccountList);
        }



        mav.addObject("myAccount", myAccount);
        mav.addObject("userName", userName);    // 모달3에 '이름' 전달

        mav.setViewName("moim/passbook");

        return mav;
    }

    @PostMapping(value = "")
    public void passbook_ok(@RequestParam("checkedBox") String checkedBox,
                                    @RequestParam(value = "selectedBank", required = false) String selectedBank,
                                    CrewDTO crewDTO, CrewMemberDTO crewMemberDTO, Model model,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        String userEmail = (String)session.getAttribute("email");

        ModelAndView mav = new ModelAndView();
        // 새로운 계좌 추가한 경우---------------------------------------------
        System.out.println("checkedBox : " + checkedBox);
        System.out.println("crewDTO.getCrew_accountid() : " + crewDTO.getCrew_accountid());
        if (checkedBox.equals("checkedNew")) {
            crewDTO.setCrew_bank(selectedBank); // 은행명
            crewDTO.setCrew_accountid(crewDTO.getCrew_accountid());   // 계좌번호
        } else {    // 기존 계좌 선택한 경우-----------------------------------
            String selectedAccount = request.getParameter("my_account");    // select box에서 선택한 '은행명 계좌번호'
            String[] parts = selectedAccount.split(" ");    // 띄어쓰기를 기준으로 나눠서 따로 insert
            if (parts.length == 2) {
                String bankName = parts[0]; // 은행명
                String accountNumber = parts[1]; // 계좌번호

                crewDTO.setCrew_bank(bankName);
                crewDTO.setCrew_accountid(accountNumber);
            }
        }

        // 목표 금액(선택): 공백인 경우 기본값 0
        if (crewDTO.getCrew_goal() == null) {
            crewDTO.setCrew_goal(0);
        } else {
            crewDTO.setCrew_goal(crewDTO.getCrew_goal());
        }

        int maxCrewNum = moimPassbookService.maxCrewNum() + 1;
        int maxCmemNum = moimPassbookService.maxCmemNum() + 1;

        crewDTO.setCrew_num(maxCrewNum);
        crewDTO.setCrew_name(crewDTO.getCrew_name());

        // 모임 소개(선택): 공백인 경우 "ㅇㅇㅇ 크루 입니다."로 설정
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
        crewMemberDTO.setCmem_num(maxCmemNum);

        moimPassbookService.insertCrewMember(crewMemberDTO);


        model.addAttribute("group",crewDTO.getCrew_name());
        model.addAttribute("num",maxCrewNum);

        JSONObject jsonResponse =  new JSONObject();
        jsonResponse.put("group",crewDTO.getCrew_name());
        jsonResponse.put("num",maxCrewNum);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
        response.getWriter().flush();

    }

    @PostMapping("/new")
    public String new_account(MyAccountDTO myAccountDTO, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String userEmail = (String)session.getAttribute("email");

        mypageService.insertAccount(userEmail,myAccountDTO.getMyaccount_anum());
        System.out.println("userEmail: " + userEmail);
        System.out.println("myAccountDTO.getMyaccount_anum(): " + myAccountDTO.getMyaccount_anum());
        return "insertNewAccount";
    }
}
