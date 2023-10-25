package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.CrewMemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrewMemberInviteMapper {

    public void insertCrewMember(CrewMemberDTO dto) throws Exception;

    public String selectCaptain(CrewMemberDTO dto) throws Exception;

    public int getCmemNumMaxNum() throws Exception;
}
