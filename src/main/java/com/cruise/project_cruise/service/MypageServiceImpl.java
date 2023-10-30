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
        return mypageMapper.deleteCrew(email,crewNum);
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
    public List<OpenBankDTO> getAccountBals(String email) throws Exception {
        return mypageMapper.getAccountBals(email);
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
    public void updateUserInfo(String tel, String address1, String address2, String email) throws Exception {
        mypageMapper.updateUserInfo(tel, address1, address2, email);
    }

    @Override
    public void updateUserPwd(String userPassword, String email) throws Exception {
        mypageMapper.updateUserPwd(userPassword, email);
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

    @Override
    public List<ScheduleDTO> getOneSchedule(String email, String scheStart) throws Exception {
        return mypageMapper.getOneSchedule(email,scheStart);
    }

    @Override
    public String getScheCrewName(String email, int crew_num) throws Exception {
        return mypageMapper.getScheCrewName(email, crew_num);
    }

    @Override
    public void deleteUser(String email) throws Exception {
        mypageMapper.deleteUser(email);
    }

    //알림 관련
    @Override
    public void insertMyAlert(int myalertNum, int crewNum, String myalertAssort, String myalertContent, String myalertAdate, String email, int boardNum) throws Exception {
        mypageMapper.insertMyAlert(myalertNum, crewNum, myalertAssort, myalertContent, myalertAdate, email, boardNum);
    }


    @Override
    public int maxMyalertNum() throws Exception {
        return mypageMapper.maxMyalertNum();
    }

    @Override
    public List<MyAlertDTO> getMyalert(String email) throws Exception {
        return mypageMapper.getMyalert(email);
    }

    @Override
    public void deleteMyalert(int myalertNum) throws Exception {
        mypageMapper.deleteMyalert(myalertNum);
    }

    @Override
    public List<MyAlertDTO> getNavAlert(String email) throws Exception {
        return mypageMapper.getNavAlert(email);
    }

    @Override
    public String getCrewNameA(int crewNum) throws Exception {
        return mypageMapper.getCrewNameA(crewNum);
    }

    @Override
    public List<CrewBoardDTO> getMyboardLink(String emil) throws Exception {
        return mypageMapper.getMyboardLink(emil);
    }


}
