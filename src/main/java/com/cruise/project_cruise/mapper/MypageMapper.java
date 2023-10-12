package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageMapper {

    public List<CrewDTO> getCrews(String email) throws Exception;
    public void deleteCrew(@Param("cmem_email")String email, @Param("crew_num") int crewNum) throws Exception;
    public  List<CrewMemberDTO> getCrewNums(String email) throws Exception;
    public List<OpenBankDTO> getOpenAccPWd(String email) throws Exception;
    public void insertAccount(@Param("email")String email,@Param("myaccount_anum") String myaccountAnum) throws Exception;
}
