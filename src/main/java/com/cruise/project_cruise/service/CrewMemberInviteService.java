package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewMemberDTO;
import org.springframework.stereotype.Service;


public interface CrewMemberInviteService {
    public void insertCrewMember(CrewMemberDTO dto) throws Exception;
    public String selectCaptain(CrewMemberDTO dto) throws Exception;

    public int getCmemNumMaxNum() throws Exception;
}