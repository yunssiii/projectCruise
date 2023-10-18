package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CrewSettingMapper {
    public List<ScheduleDTO> getCrewScheList(@Param("crew_num") int crewNum) throws Exception;
    public void insertCrewSche(ScheduleDTO scheduleDTO) throws Exception;
    public void updateCrewSche(ScheduleDTO scheduleDTO) throws Exception;
    public void deleteCrewSche(@Param("sche_num") int scheNum) throws Exception;
    public int getScheMaxNum() throws Exception;


}
