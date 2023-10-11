package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import com.cruise.project_cruise.dto.TemplateDTO;
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
    public CrewMemberDTO deleteCrew(String email, int crewNum) throws Exception {
        return mypageMapper.deleteCrew(email,crewNum);
    }


}
