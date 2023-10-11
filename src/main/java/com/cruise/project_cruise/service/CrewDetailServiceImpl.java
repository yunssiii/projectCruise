package com.cruise.project_cruise.service;

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
    public void deleteCrewMember
            (@Param("memberEmail") String cmemEmail, @Param("crewNum") int crewNum) throws Exception {

        crewDetailMapper.deleteCrewMember(cmemEmail, crewNum);
    };

    // Mapper Interface를 그대로 가져와주기.

}
