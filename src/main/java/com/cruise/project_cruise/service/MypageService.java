package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface MypageService {

    public List<CrewDTO> getCrews(String email) throws Exception; //내 크루 보기
    public CrewMemberDTO deleteCrew(String email,int crewNum) throws Exception; //크루 1개 탈퇴하기

}
