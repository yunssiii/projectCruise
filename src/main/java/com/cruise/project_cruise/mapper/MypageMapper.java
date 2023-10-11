package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MypageMapper {

    public List<CrewDTO> getCrews(String email) throws Exception;
    public CrewMemberDTO deleteCrew(String email,int crewNum) throws Exception;


}
