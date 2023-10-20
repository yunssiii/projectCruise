package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.TemplateDTO;
import com.cruise.project_cruise.mapper.CrewDetailMapper;
import com.cruise.project_cruise.mapper.TemplateMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrewDetailServiceImpl implements CrewDetailService {

    @Autowired
    private CrewDetailMapper crewDetailMapper;

    @Override
    public boolean isMember(int crewNum, String userEmail) throws Exception {
        int isMember = crewDetailMapper.isMember(crewNum,userEmail);

        if(isMember==1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isCaptain(int crewNum, String userEmail) throws Exception {
        int isCaptain = crewDetailMapper.isCaptain(crewNum,userEmail);
        if(isCaptain==1) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteCrewMember
            (String cmemEmail, int crewNum) throws Exception {
        crewDetailMapper.deleteCrewMember(cmemEmail, crewNum);
    }

    @Override
    public CrewDTO getCrewData(int crewNum) throws Exception {
        return crewDetailMapper.getCrewData(crewNum);
    }

    @Override
    public String getCaptainName(String captainEmail) throws Exception {
        return crewDetailMapper.getCaptainName(captainEmail);
    }
    @Override
    public int getAccountBalance(String crewAccount) throws Exception {
        return crewDetailMapper.getAccountBalance(crewAccount);
    }

    // Mapper Interface를 그대로 가져와주기.

}
