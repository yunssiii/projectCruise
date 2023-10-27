package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.CrewAlertDTO;
import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

@Mapper
public interface CrewAlertMapper {
    public void insertCrewAlert(@Param("cAlertNum") int cAlertNum, @Param("crewNum") int crewNum,
                                @Param("cAlertAssort") String cAlertAssort,@Param("cAlertContent") String cAlertContent,
                                @Param("cAlertAlertDate") String cAlertAlertDate) throws Exception;
    public int cAlertMaxNum() throws Exception;
    public List<CrewAlertDTO> getNewMemberNewsList(@Param("crewNum") int crewNum) throws Exception;
    public List<CrewAlertDTO> getNewCrewNewsList(@Param("crewNum") int crewNum) throws Exception;
    public List<CrewAlertDTO> getAllNewsList(@Param("crewNum") int crewNum) throws Exception;

}
