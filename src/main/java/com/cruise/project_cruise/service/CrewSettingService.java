package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.ScheduleDTO;
import com.cruise.project_cruise.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface CrewSettingService {
    // red 크루 관리
    // 크루 정보수정
    public List<ScheduleDTO> getCrewScheList(@Param("crew_num") int crewNum) throws Exception;
    public UserDTO getUser(@Param("email") String userEmail) throws Exception;

    // 항해 중단하기
    public void stopSailing(@Param("crewNum") int crewNum) throws Exception;
    public void cancelStopSailing(@Param("crewNum") int crewNum) throws Exception;
    public void deleteCrew(@Param("crewNum") int crewNum) throws Exception;


    // 크루 일정관리
    public void updateCrewInfo(CrewDTO crewDTO) throws Exception;
    public void insertCrewSche(ScheduleDTO scheduleDTO) throws Exception;
    public void updateCrewSche(ScheduleDTO scheduleDTO) throws Exception;
    public void deleteCrewSche(@Param("sche_num") int scheNum) throws Exception;
    public int getScheMaxNum() throws Exception;


    // red 선원 관리
    public List<Map<String,String>> getCrewMemberList(@Param("crew_num") int crewNum) throws Exception;
    public void deleteMember(@Param("email") String banEmail,@Param("crew_num") int crewNum) throws Exception;

    // 잔액 1/N하기
    public int getMemberPayCountSum(@Param("crewNum") int crewNum) throws Exception;
}
