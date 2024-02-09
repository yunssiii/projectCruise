package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewAlertDTO;

import java.util.Date;
import java.util.List;

public interface CrewAlertService {
    public void insertCrewAlert(int cAlertNum, int crewNum,
                                String cAlertAssort,String cAlertContent,
                                Date cAlertAlertDate) throws Exception;
    public int cAlertMaxNum() throws Exception;
    public List<CrewAlertDTO> getNewMemberNewsList(int crewNum) throws Exception;
    public List<CrewAlertDTO> getNewCrewNewsList(int crewNum) throws Exception;
    public List<CrewAlertDTO> getAllNewsList(int crewNum) throws Exception;
    String getDateString(Date date);
}
