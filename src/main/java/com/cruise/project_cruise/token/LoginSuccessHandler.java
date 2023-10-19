package com.cruise.project_cruise.token;

import com.cruise.project_cruise.dto.UserDTO;
import com.cruise.project_cruise.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;

    @Value("${jwt.key.secret.access.expiration}")
    private String accessTokenExpiration;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출

        String accessToken = jwtTokenizer.createAccessToken(email); // JwtㅅTokenizer의 createAccessToken을 사용하여 AccessToken 발급
        String refreshToken = jwtTokenizer.createRefreshToken(); // JwtTokenizer의 createRefreshToken을 사용하여 RefreshToken 발급

        jwtTokenizer.sendAccessAndRefreshToken(response, accessToken, refreshToken); // 응답 헤더에 AccessToken, RefreshToken 실어서 응답


        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(userService.selectEmail(email));
            if(userDTO.getEmail()!=null){
                userDTO.setRefreshToken(refreshToken);
                userService.updateUserRefreshToken(userDTO);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);

        //response.setHeader("accessToken",accessToken);

    }





    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
