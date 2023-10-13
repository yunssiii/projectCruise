package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface MypageService {

    public List<CrewDTO> getCrews(String email) throws Exception; //내 크루 보기
    public void deleteCrew(String email,int crewNum) throws Exception; //크루 1개 탈퇴하기
    public  List<CrewMemberDTO> getCrewNums(String email) throws Exception; //zero,all 구분 위해 크루넘 한 개 가져오기
    public List<OpenBankDTO> getOpenAccPWd(String email) throws Exception; //가상계좌 비번 여러개 가져오기

    public void insertAccount(@Param("email")String email,@Param("myaccount_anum") String myaccountAnum) throws Exception; //계좌 등록하기
    public  List<OpenBankDTO> getAccounts(String email) throws Exception; //계좌 조회
    public String getWebpassword(String email) throws Exception; //이체 비밀번호 조회
    public void updateWebpassword(@Param("pay_password") String payPwd, @Param("email")String email) throws Exception; //이제 비밀번호 수정(등록/변경)
    public UserDTO getUserName(String email) throws Exception; //로그인한 사용자 이름 조회
    public UserDTO getUserInfo(String email) throws Exception; //로그인한 사용자 정보 조회
}
