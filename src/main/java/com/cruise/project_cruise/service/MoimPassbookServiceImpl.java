package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.mapper.MoimPassbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoimPassbookServiceImpl implements MoimPassbookService {

    @Autowired
    private MoimPassbookMapper mapper;

    @Override
    public int maxCrewNum() throws Exception {
        return mapper.maxCrewNum();
    }

    @Override
    public void insertCrew(CrewDTO dto) throws Exception {
        mapper.insertCrew(dto);
    }

    @Override
    public void insertCrewMember(CrewMemberDTO dto) throws Exception {
        mapper.insertCrewMember(dto);
    }

    @Override
    public List<MyAccountDTO> getMyAccount(String email) throws Exception {
        return mapper.getMyAccount(email);
    }

    @Override
    public String getBankName(String myaccount_anum) throws Exception {
        return mapper.getBankName(myaccount_anum);
    }

    @Override
    public int maxCmemNum() throws Exception {
        return mapper.maxCmemNum();
    }
}
