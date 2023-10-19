package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.ScheduleDTO;
import com.cruise.project_cruise.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CrewSettingMapper {
    public List<ScheduleDTO> getCrewScheList(@Param("crew_num") int crewNum) throws Exception;
    public UserDTO getCrewCaptain(@Param("captainEmail") String captainEmail) throws Exception;
    public void updateCrewInfo(CrewDTO crewDTO) throws Exception;
    public void insertCrewSche(ScheduleDTO scheduleDTO) throws Exception;
    public void updateCrewSche(ScheduleDTO scheduleDTO) throws Exception;
    public void deleteCrewSche(@Param("sche_num") int scheNum) throws Exception;
    public int getScheMaxNum() throws Exception;


}
