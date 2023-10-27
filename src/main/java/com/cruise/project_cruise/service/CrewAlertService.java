package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CrewAlertService {
    public void insertCrewAlert(int calertNum, int crewNum,
                                String calertAssort,String calertContent,
                                String calertAlertdate) throws Exception;
    public int cAlertMaxNum() throws Exception;
}
