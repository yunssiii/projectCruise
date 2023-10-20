package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import com.cruise.project_cruise.mapper.MypageMapper;
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
    public String getOneCaptain(String email, int crewNum) throws Exception {
        return mypageMapper.getOneCaptain(email, crewNum);
    }

    @Override
    public int deleteCrew(String email, int crewNum) throws Exception {
        int crewResult =  mypageMapper.deleteCrew(email,crewNum);
        return crewResult;
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

    @Override
    public List<MyAccountDTO> getAccountList(String email) throws Exception {
        return mypageMapper.getAccountList(email);
    }

    @Override
    public List<MyAccountDTO> getOneAccount(String email, String myaccountName) throws Exception {
        return mypageMapper.getOneAccount(email, myaccountName);
    }

    @Override
    public List<OpenBankUsingDTO> getUseAccounts(String accountNum, int monthNum) throws Exception {
        return mypageMapper.getUseAccounts(accountNum, monthNum);
    }

    @Override
    public void updateAname(String myaccountName, String myaccountNum) throws Exception {
        mypageMapper.updateAname(myaccountName,myaccountNum);
    }

    @Override
    public void deleteMyaccount(String myaccountNum) throws Exception {
       mypageMapper.deleteMyaccount(myaccountNum);
    }

    @Override
    public String getWebpassword(String email) throws Exception {
        return mypageMapper.getWebpassword(email);
    }

    @Override
    public void updateWebpassword(String payPwd,String email) throws Exception {
        mypageMapper.updateWebpassword(payPwd,email);
    }

    @Override
    public UserDTO getUserInfo(String email) throws Exception {
        return mypageMapper.getUserInfo(email);
    }

    @Override
    public String getCrewName(int crewNum) throws Exception {
        return mypageMapper.getCrewName(crewNum);
    }

    @Override
    public List<CrewBoardDTO> getMyboard(String email, int start, int end) throws Exception {
        return mypageMapper.getMyboard(email,start,end);
    }


    @Override
    public void deleteMyboard(int boardNum) throws Exception {
        mypageMapper.deleteMyboard(boardNum);
    }

    @Override
    public List<CrewCommentDTO> getMyComment(String email) throws Exception {
        return mypageMapper.getMyComment(email);
    }


    @Override
    public String getBoardSubject(int boardNum) throws Exception {
        return mypageMapper.getBoardSubject(boardNum);
    }

    @Override
    public void deleteMycomment(int commentNum) throws Exception {
        mypageMapper.deleteMycomment(commentNum);
    }

    @Override
    public int getBoardCount(String email) throws Exception {
        return mypageMapper.getBoardCount(email);
    }

    @Override
    public List<ScheduleDTO> getSchedule(String email) throws Exception {
        return mypageMapper.getSchedule(email);
    }


}
