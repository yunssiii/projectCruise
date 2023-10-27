package com.cruise.project_cruise.service;

import com.cruise.project_cruise.mapper.CrewAlertMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrewAlertServiceImpl implements CrewAlertService{

    @Autowired
    private CrewAlertMapper crewAlertMapper;

    @Override
    public void insertCrewAlert(int calertNum, int crewNum,
                                String calertAssort,String calertContent,
                                String calertAlertdate) throws Exception {

    }

    @Override
    public int cAlertMaxNum() throws Exception {
        return crewAlertMapper.cAlertMaxNum();
    }
}
