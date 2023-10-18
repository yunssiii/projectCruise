package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.ScheduleDTO;
import com.cruise.project_cruise.mapper.CrewSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrewSettingServiceImpl implements CrewSettingService {

    @Autowired
    private CrewSettingMapper crewSettingMapper;

    @Override
    public List<ScheduleDTO> getCrewScheList(int crewNum) throws Exception {
        return crewSettingMapper.getCrewScheList(crewNum);
    }

    @Override
    public void insertCrewSche(ScheduleDTO scheduleDTO) throws Exception {
        crewSettingMapper.insertCrewSche(scheduleDTO);
    }

    @Override
    public void updateCrewSche(ScheduleDTO scheduleDTO) throws Exception {
        crewSettingMapper.updateCrewSche(scheduleDTO);
    }

    @Override
    public void deleteCrewSche(int scheNum) throws Exception {
        crewSettingMapper.deleteCrewSche(scheNum);
    }

    @Override
    public int getScheMaxNum() throws Exception {
        return crewSettingMapper.getScheMaxNum();
    }
}
