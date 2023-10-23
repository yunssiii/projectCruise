package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CrewDetailMapper {
    public int isMember(@Param("crewNum") int crewNum, @Param("userEmail") String userEmail) throws Exception;
    public int isCaptain(@Param("crewNum") int crewNum, @Param("userEmail") String userEmail) throws Exception;
    public List<MyAccountDTO> getUserAccountList(@Param("userEmail") String userEmail) throws Exception;
    public void deleteCrewMember(@Param("memberEmail") String cmemEmail, @Param("crewNum") int crewNum) throws Exception;
    public CrewDTO getCrewData(int crewNum) throws Exception;
    public String getCaptainName(String captainEmail) throws Exception;
    public int getAccountBalance(String crewAccount) throws Exception;
    public Map<String,Object> getCrewUserInfo(@Param("crewNum")int crewNum, @Param("userEmail") String userEmail) throws Exception;

}
