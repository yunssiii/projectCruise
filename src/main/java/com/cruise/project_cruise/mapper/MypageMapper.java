package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface MypageMapper {

    public List<CrewDTO> getCrews(String email) throws Exception;
    public String getOneCaptain(@Param("email")String email,@Param("crew_num")int crewNum) throws Exception;
    public int deleteCrew(@Param("cmem_email")String email, @Param("crew_num") int crewNum) throws Exception;
    public  List<CrewMemberDTO> getCrewNums(String email) throws Exception;
    public List<OpenBankDTO> getOpenAccPWd(String email) throws Exception;
    public void insertAccount(@Param("email")String email,@Param("myaccount_anum") String myaccountAnum) throws Exception;
    public  List<MyAccountDTO> getAccountList(String email) throws Exception;
    public List<OpenBankDTO> getAccountBals(String email) throws Exception;
    public  List<MyAccountDTO> getOneAccount(@Param("email")String email,@Param("myaccount_anum")String myaccountName) throws Exception;
    public List<OpenBankUsingDTO> getUseAccounts(@Param("accountNum") String accountNum, @Param("monthNum") int monthNum) throws Exception;
    public void updateAname(@Param("myaccount_name") String myaccountName,@Param("myaccount_anum")String myaccountNum) throws Exception;
    public void deleteMyaccount(String myaccountNum) throws Exception;
    public String getWebpassword(String email) throws Exception;
    public void updateWebpassword(@Param("pay_password") String payPwd, @Param("email")String email) throws Exception;
    public UserDTO getUserInfo(String email) throws Exception;
    public void updateUserInfo(@Param("tel")String tel,@Param("address1")String address1,
                               @Param("address2")String address2,@Param("email")String email) throws Exception;
    public void updateUserPwd(@Param("user_password")String userPassword,@Param("email")String email) throws Exception;
    public String getCrewName(@Param("crew_num") int crewNum) throws Exception;
    public List<CrewBoardDTO> getMyboard(@Param("email")String email,@Param("start") int start,@Param("end") int end) throws Exception;
    public void deleteMyboard(int boardNum) throws Exception;
    public List<CrewCommentDTO> getMyComment(@Param("email")String email) throws Exception;
    public String getBoardSubject(int boardNum) throws Exception;
    public void deleteMycomment(int commentNum) throws Exception;
    public  int getBoardCount(String email) throws Exception;
    public int getCommentCount(String email) throws Exception;
    public List<ScheduleDTO> getSchedule(String email) throws Exception;
    public List<ScheduleDTO> getOneSchedule(@Param("email") String email,@Param("sche_start") String scheStart) throws Exception;
    public String getScheCrewName(@Param("email") String email,@Param("crew_num")int crew_num) throws Exception;
    public void deleteUser(String email) throws Exception;

    //알림 관련

    public void insertMyAlert(@Param("myalert_num")int myalertNum,@Param("crew_num") int crewNum,@Param("myalert_assort")String myalertAssort,
                              @Param("myalert_content")String myalertContent,@Param("myalert_adate")String myalertAdate,
                              @Param("email")String email,@Param("board_num")int boardNum) throws Exception;
    public int maxMyalertNum() throws Exception;
    public List<MyAlertDTO> getMyalert(String email) throws Exception;
    public void deleteMyalert(int myalertNum) throws Exception;
    public List<MyAlertDTO> getNavAlert(String email) throws Exception;

    public String getCrewNameA(int crewNum) throws Exception;

    public List<CrewBoardDTO> getMyboardLink(String emil) throws Exception;

}
