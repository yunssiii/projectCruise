package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewAlertDTO;
import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CrewAlertService {
    public void insertCrewAlert(int cAlertNum, int crewNum,
                                String cAlertAssort,String cAlertContent,
                                String cAlertAlertDate) throws Exception;
    public int cAlertMaxNum() throws Exception;
    public List<CrewAlertDTO> getNewMemberNewsList(int crewNum) throws Exception;
    public List<CrewAlertDTO> getNewCrewNewsList(int crewNum) throws Exception;
    public List<CrewAlertDTO> getAllNewsList(int crewNum) throws Exception;
}
