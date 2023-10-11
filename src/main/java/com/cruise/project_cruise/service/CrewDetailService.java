package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.TemplateDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface CrewDetailService {
    // Mapper Interface를 그대로 가져와주기.

    public void deleteCrewMember(String cmemEmail, int crewNum) throws Exception;
    public CrewDTO getCrewData(int crewNum) throws Exception;
    public String getCaptainName(String captainEmail) throws Exception;
    public int getAccountBalance(String crewAccount) throws Exception;

}
