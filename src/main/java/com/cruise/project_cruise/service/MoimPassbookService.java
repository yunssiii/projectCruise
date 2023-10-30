package com.cruise.project_cruise.service;


import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;

import java.util.List;

public interface MoimPassbookService {

    public int maxCrewNum() throws Exception;
    public void insertCrew(CrewDTO dto) throws Exception;
    public void insertCrewMember(CrewMemberDTO dto) throws Exception;
    public List<MyAccountDTO> getMyAccount(String email) throws Exception;
    public String getBankName(String myaccount_anum) throws Exception;
    public int maxCmemNum() throws Exception;

}
