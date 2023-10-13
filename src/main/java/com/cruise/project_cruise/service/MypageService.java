package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MypageService {

    public List<CrewDTO> getCrews(String email) throws Exception; //내 크루 보기
    public void deleteCrew(String email,int crewNum) throws Exception; //크루 1개 탈퇴하기
    public  List<CrewMemberDTO> getCrewNums(String email) throws Exception; //zero,all 구분 위해 크루넘 한 개 가져오기
    public List<OpenBankDTO> getOpenAccPWd(String email) throws Exception; //가상계좌 비번 여러개 가져오기

    public void insertAccount(@Param("email")String email,@Param("myaccount_anum") String myaccountAnum) throws Exception; //계좌 등록하기
}
