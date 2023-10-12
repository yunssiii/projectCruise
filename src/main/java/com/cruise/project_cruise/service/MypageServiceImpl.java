package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.mapper.MypageMapper;
import com.cruise.project_cruise.mapper.TemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MypageServiceImpl implements MypageService {

    @Autowired
    private MypageMapper mypageMapper;


    @Override
    public List<CrewDTO> getCrews(String email) throws Exception {
        return mypageMapper.getCrews(email);
    }

    @Override
    public void deleteCrew(String email, int crewNum) throws Exception {
        mypageMapper.deleteCrew(email,crewNum);
    }

    @Override
    public List<CrewMemberDTO> getCrewNums(String email) throws Exception {
        return mypageMapper.getCrewNums(email);
    }

    @Override
    public List<OpenBankDTO> getOpenAccPWd(String email) throws Exception {
        return mypageMapper.getOpenAccPWd(email);
    }

    @Override
    public void insertAccount(String email, String myaccountAnum) throws Exception {
        mypageMapper.insertAccount(email,myaccountAnum);
    }


}
