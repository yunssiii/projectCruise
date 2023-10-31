package com.cruise.project_cruise.security.config;

import com.cruise.project_cruise.dto.UserDTO;
import com.cruise.project_cruise.oauth.config.PrincipalDetails;
import com.cruise.project_cruise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticaionSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); //현재 로그인한 사용자 정보 가져오기

        if (userDetails instanceof PrincipalDetails) {

            PrincipalDetails principalDetails = (PrincipalDetails) userDetails;
            UserDTO userDTO = principalDetails.getUserDTO();
            System.out.println("CustomSuccessHandler: " + userDTO);
            boolean isNewUser = principalDetails.isNewUser();
            String tel = null;

            try {
                tel = userService.selectTel(userDTO.getEmail());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (isNewUser || tel == null) {
                response.sendRedirect("/nextSocialSignUpForm");
            } else {



                Cookie cookie = new Cookie("lastLoginMethod",userDTO.getProvider());
                cookie.setDomain("192.168.16.27");
                cookie.setPath("/");

                cookie.setMaxAge(60*60*60*24);
                cookie.setSecure(true);


                response.addCookie(cookie);

                response.sendRedirect("/mypage/mypage_all");
            }


        }

    }
}






