package com.cruise.project_cruise.security.config;

//1.코드받기(인증),2.엑세스토큰받기(권한),3.사용자프로필 정보 가져오기 4.그 정보를 토대로 회원가입진행

import com.cruise.project_cruise.oauth.config.PrincipalDetailsService;
import com.cruise.project_cruise.oauth.config.PrincipalOauth2UserService;
import com.cruise.project_cruise.service.UserService;
import com.cruise.project_cruise.token.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@EnableWebSecurity  //Spring Security 설정 활성화
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true) //secured 어노테이션 활성화 이걸로 권한있는사람만 들어가게 할수있음
public class WebSecurityConfigure implements WebMvcConfigurer {


    @Bean
    public ServletContextInitializer clearJsession(){ //해당 세션 쿠키가 Http통해서만 접근가능하도록함 보안강화(javascript등 클라이언트 측의 접근 방지)
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
                SessionCookieConfig sessionCookieConfig=servletContext.getSessionCookieConfig();
                sessionCookieConfig.setHttpOnly(true);
            }
        };
    }


    public WebSecurityConfigure(PrincipalDetailsService principalDetailsService) {
        this.principalDetailsService = principalDetailsService;
    }

    //Bean 적어주면 해당 메서드의 리턴되는 오브젝터를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() throws Exception {
        return new JwtAuthenticationProcessingFilter(passwordUtil, jwtTokenizer, userService);
    }

    @Autowired
    private CustomAuthenticaionSuccessHandler customAuthenticaionSuccessHandler;

    PasswordUtil passwordUtil;
    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(jwtTokenizer,userService);
    }

    private final PrincipalDetailsService principalDetailsService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{
        http

                .formLogin().disable()

                .httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/**","/*").permitAll()
                .anyRequest().permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) //로그아웃하면 security session과 httpsession 같이 삭제되는거 방지
                .permitAll()
                .and()
                .oauth2Login()
                .loginPage("/signup")
                .successHandler(customAuthenticaionSuccessHandler)//구글 로그인이 완료된 뒤의 후처리가 필요함. Tip.(액세스토큰+사용자프로필정보를 받음)//defaultsuccessurl-- login으로 와서 로그인 완료하면 /가지만 다른 url로 온경우에는 그 Url로 보내
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

    http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
    http.addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        return http.build();
    }



    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter(){
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        return customJsonUsernamePasswordAuthenticationFilter;
    }


    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encodePwd());
        provider.setUserDetailsService(principalDetailsService);
        return new ProviderManager(provider);
    }
}


