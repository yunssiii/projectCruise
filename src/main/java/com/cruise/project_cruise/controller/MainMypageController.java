package com.cruise.project_cruise.controller;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;

import com.cruise.project_cruise.dto.MyAccountDTO;

import com.cruise.project_cruise.dto.UserDTO;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.service.MypageService;
import com.cruise.project_cruise.token.JwtTokenizer;
import com.cruise.project_cruise.util.CrewBoardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainMypageController {

    @Autowired
    private MypageService mypageService;

    @Autowired
    JwtTokenizer jwtTokenizer;

    @Autowired
    CrewBoardUtil myUtil;



    /*
        로그인 후 바로 연결되는 마이페이지 메인창 메소드
        크루와 계좌 0이면 zero페이지 보여지고
        0 이상이면 all페이지 보여짐
        --
        크루 조회, 계좌 조회, 계좌 등록, 초대 구분
     */
    @RequestMapping(value = "/mypage/mypage_all",method = {RequestMethod.GET, RequestMethod.POST})

    public ModelAndView all(HttpSession session, HttpServletRequest request, @RequestParam(required = false) String anum,
                            @RequestParam(required = false) String aPwd,
                            @RequestHeader(value = "Authorization", required = false) String accessToken ,
                            @AuthenticationPrincipal OAuth2User principal, Principal principal2) throws  Exception {



        String email = null;
        Optional<String> emailOptional = jwtTokenizer.extractEmail(accessToken);

        String group = (String) session.getAttribute("group");
        String num = (String) session.getAttribute("num");

        //초대받았을때와 아닐때를 구분 할 수 있음
        if (group == null && num == null) { //초대받지 않았고 로그인을하면 group과 num을하면 지워지므로 세션값으로 다시 비교

            if (principal != null || emailOptional.isPresent()) { // 로그인 했을 경우
                System.out.println("초대받지 못한 로그인");
                if (emailOptional.isPresent()) { // 일반 로그인
                    email = emailOptional.get();
                    System.out.println(email);
                    session.setAttribute("email", email);
                } else { // 소셜 로그인
                    Map<String, Object> attributes = principal.getAttributes();

                    if (attributes.get("kakao_account") != null) { // 카카오 로그인
                        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                        email = (String) kakaoAccount.get("email");
                    } else if (attributes.get("response") != null) { // 네이버 로그인
                        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");
                        email = (String) naverAccount.get("email");
                    } else { //구글 로그인
                        email = (String) attributes.get("email");
                    }
                }
            }
        }else { //초대받았을때

            if (principal != null || emailOptional.isPresent()) { // 로그인 했을 경우
                System.out.println("초대받은 로그인");
                if (emailOptional.isPresent()) { // 일반 로그인
                    System.out.println("일반로그인");
                    email = emailOptional.get();
                    session.setAttribute("email", email);
                } else { // 소셜 로그인
                    Map<String, Object> attributes = principal.getAttributes();
                    System.out.println("소셜로그인");

                    if (attributes.get("kakao_account") != null) { // 카카오 로그인
                        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                        email = (String) kakaoAccount.get("email");
                    } else if (attributes.get("response") != null) { // 네이버 로그인
                        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("response");
                        email = (String) naverAccount.get("email");
                    } else { //구글 로그인
                        email = (String) attributes.get("email");
                    }
                }
            }
        }

        if (session.getAttribute("email")==null){
            session.setAttribute("email", email);
            email = (String) session.getAttribute("email");
        }else {
            email = (String) session.getAttribute("email");
            session.setAttribute("email",email);
        }

        System.out.println("뒷부분: "+ email);

        List<CrewDTO> crewLists = mypageService.getCrews(email); //크루 정보
        List<CrewMemberDTO> crewNumLists = mypageService.getCrewNums(email); //크루맴버의 크루번호
        List<OpenBankDTO> openAccPwd = mypageService.getOpenAccPWd(email); //가상계좌 비밀번호
        List<MyAccountDTO> accountLists = mypageService.getAccountList(email); //등록된 계좌정보
        List<OpenBankDTO> accountBalLists = mypageService.getAccountBals(email); //가상 계좌의 잔액만
        UserDTO userInfo = mypageService.getUserInfo(email); // 로그인한 사용자 정보.이름
        List<MyAccountDTO> myaccountList = mypageService.getAccountList(email); //등록된 계좌 정보 조회

        ModelAndView mav = new ModelAndView();

        //크루, 계좌가 있으면 all 페이지
        if(!crewNumLists.isEmpty() || !accountLists.isEmpty()){
            mav.setViewName("mypage/mypage_all");

            mav.addObject("crewLists",crewLists);
            mav.addObject("userInfo",userInfo);
            mav.addObject("myaccountList",myaccountList);

            //계좌 비밀번호가 있으면 계좌 비밀번호 넘기기
            if(openAccPwd != null){
                mav.addObject("openAccPwd",openAccPwd);
            }

            //가상 계좌 잔액을 내 계좌 잔액dto에 set
            for(int i=0;i<accountBalLists.size();i++){
                int balance = accountBalLists.get(i).getOpen_balance();
                accountLists.get(i).setMyaccount_balance(balance);
            }

            mav.addObject("accountLists",accountLists);

        }else { //크루, 계좌 없으면 zero
            mav.setViewName("mypage/mypageZero");
        }

        //계좌번호 있으면서 오픈뱅킹의 비밀번호와 입력값이 같으면 계좌 등록
        for(int i=0;i<openAccPwd.size();i++){

            if(anum !=null && aPwd != null && openAccPwd.get(i).getOpen_password().equals(aPwd)){

                mypageService.insertAccount(email,anum);
                mav.setViewName("redirect:/mypage/mypage_all");
                return mav;

            }
        }





        return mav; //프론트에서 요청했을때는 이 리턴이 프론트로 가는듯 그래서 화면이 안나오는것 같음
    }


    /*
    @GetMapping("/test")
    public ModelAndView test() {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("/sse");

        return mav;
    }

    @GetMapping("/test2")
    public ModelAndView test2() {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("/sseTest");

        return mav;
    }

    @GetMapping("/test3")
    public ModelAndView test3() {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("/toastTest");

        return mav;
    }

    @GetMapping("/test5")
    public ModelAndView test5() {

        ModelAndView mav = new ModelAndView();

        mav.setViewName("/nextSseTest");

        return mav;
    }

    @PostMapping("/test")
    public ModelAndView testInsert(@RequestParam("nums") int num,@RequestParam("assorts") String assort,
                           @RequestParam("contents")String content,@RequestParam("dates")String date,
                           @RequestParam("emails")String email) throws Exception{

        System.out.println("왔다 >>>>>>>>");

        mypageService.insertMyAlert(num,assort,content,date,email);

        ModelAndView mav = new ModelAndView();

        mav.setViewName("redirect:/test");

        return mav;

    }
*/


    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(HttpServletRequest request, Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("error/myPageError"); // myPageError.html 뷰를 생성하여 원하는 에러 처리 화면을 구현할 수 있습니다.
        return mav;
    }


}


