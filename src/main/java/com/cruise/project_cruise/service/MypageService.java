package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MypageService {

    public List<CrewDTO> getCrews(String email) throws Exception; //내 크루 전체 조회
    public String getOneCaptain(@Param("email")String email,@Param("crew_num")int crewNum) throws Exception; //내 크루 캡틴이메일 하나만 조회
    public int deleteCrew(String email, int crewNum) throws Exception; //크루 1개 탈퇴하기
    public  List<CrewMemberDTO> getCrewNums(String email) throws Exception; //zero,all 구분 위해 크루넘 한 개 가져오기
    public List<OpenBankDTO> getOpenAccPWd(String email) throws Exception; //가상계좌 비번 여러개 가져오기
    public void insertAccount(@Param("email")String email,@Param("myaccount_anum") String myaccountAnum) throws Exception; //계좌 등록하기
    public  List<MyAccountDTO> getAccountList(String email) throws Exception; //계좌 전체 조회
    public List<OpenBankDTO> getAccountBals(String email) throws Exception; //계좌 전체 중 잔액만 조회
    public  List<MyAccountDTO> getOneAccount(@Param("email")String email,@Param("myaccount_anum")String myaccountName) throws Exception; //계좌 하나 조회
    public List<OpenBankUsingDTO> getUseAccounts(@Param("accountNum") String accountNum, @Param("monthNum") int monthNum) throws Exception; //계좌 내역 조회
    public void updateAname(@Param("myaccount_name") String myaccountName,@Param("myaccount_anum")String myaccountNum) throws Exception; //계좌명 수정
    public void deleteMyaccount(String myaccountNum) throws Exception; //등록된 계좌 삭제
    public String getWebpassword(String email) throws Exception; //이체 비밀번호 조회
    public void updateWebpassword(@Param("pay_password") String payPwd, @Param("email")String email) throws Exception; //이제 비밀번호 수정(등록/변경)
    public UserDTO getUserInfo(String email) throws Exception; //로그인한 사용자 정보 조회
    public void updateUserInfo(@Param("tel")String tel,@Param("address1")String address1,
                               @Param("address2")String address2,@Param("email")String email) throws Exception; //내 정보 수정
    public void updateUserPwd(@Param("user_password")String userPassword,@Param("email")String email) throws Exception; //내 정보 비밀번호만! 수정
    public String getCrewName(@Param("crew_num") int crewNum) throws Exception; // 크루명 조회
    public List<CrewBoardDTO> getMyboard(@Param("email")String email,@Param("start") int start,@Param("end") int end) throws Exception; //내 게시글 조회
    public void deleteMyboard(int boardNum) throws Exception; //내 게시글 삭제
    public List<CrewCommentDTO> getMyComment(@Param("email")String email) throws Exception; //내 댓글 조회
    public String getBoardSubject(int boardNum) throws Exception; //게시글 제목 조회
    public void deleteMycomment(int commentNum) throws Exception; //댓글 삭제
    public  int getBoardCount(String email) throws Exception; //게시글 전체 수
    public List<ScheduleDTO> getSchedule(String email) throws Exception; //일정 조회
    public List<ScheduleDTO> getOneSchedule(@Param("email") String email,@Param("sche_start") String scheStart) throws Exception; //일정 하루 조회
    public String getScheCrewName(@Param("email") String email,@Param("crew_num")int crew_num) throws Exception;
    public void deleteUser(String email) throws Exception; //회원 탈퇴

    //알림 관련
    public void insertMyAlert(@Param("myalert_num")int myalertNum,@Param("crew_num") int crewNum,@Param("myalert_assort")String myalertAssort,
                              @Param("myalert_content")String myalertContent,@Param("myalert_adate")String myalertAdate,
                              @Param("email")String email,@Param("board_num")int boardNum) throws Exception; //내 알림 insert
    public int maxMyalertNum() throws Exception; //내 알림 maxNum
    public List<MyAlertDTO> getMyalert(String email) throws Exception; //내 알림 조회
    public void deleteMyalert(int myalertNum) throws Exception; //내 알림 삭제
    public List<MyAlertDTO> getNavAlert(String email) throws Exception; //네비바 알림 조회

    public String getCrewNameA(int crewNum) throws Exception; //크루이름 조회

    public List<CrewBoardDTO> getMyboardLink(String emil) throws Exception; //링크용 게시글 조회
}
