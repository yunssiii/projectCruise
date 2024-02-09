package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewAlertDTO;
import com.cruise.project_cruise.mapper.CrewAlertMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CrewAlertServiceImpl implements CrewAlertService{

    @Autowired
    private CrewAlertMapper crewAlertMapper;

    @Override
    public void insertCrewAlert(int cAlertNum, int crewNum,
                                String cAlertAssort,String cAlertContent,
                                Date cAlertAlertDate) throws Exception {
        String cAlertAlertDateStr = getDateString(cAlertAlertDate);
        crewAlertMapper.insertCrewAlert(cAlertNum,crewNum,cAlertAssort,cAlertContent,cAlertAlertDateStr);
    }

    @Override
    public int cAlertMaxNum() throws Exception {
        return crewAlertMapper.cAlertMaxNum();
    }

    @Override
    public List<CrewAlertDTO> getNewMemberNewsList(int crewNum) throws Exception {
        return crewAlertMapper.getNewMemberNewsList(crewNum);
    }

    @Override
    public List<CrewAlertDTO> getNewCrewNewsList(int crewNum) throws Exception {
        return crewAlertMapper.getNewCrewNewsList(crewNum);
    }

    @Override
    public List<CrewAlertDTO> getAllNewsList(int crewNum) throws Exception {
        return crewAlertMapper.getAllNewsList(crewNum);
    }

    @Override
    public String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

}
