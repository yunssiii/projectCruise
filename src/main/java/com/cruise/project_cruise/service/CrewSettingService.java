package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CrewSettingService {
    public List<ScheduleDTO> getCrewScheList(@Param("crew_num") int crewNum) throws Exception;
}
