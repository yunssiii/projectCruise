package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.*;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import com.cruise.project_cruise.mapper.MypageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MypageServiceImpl implements MypageService {

    @Autowired
    private MypageMapper mypageMapper;

    @Autowired
    CrewBoardUtil myUtil;


    @Override
    public List<CrewDTO> getCrews(String email) throws Exception {
        return mypageMapper.getCrews(email);
    }

    @Override
    public String getOneCaptain(String email, int crewNum) throws Exception {

        //String return
        String Response = ""; //응답 변수
        int delcrews = 0; //삭제 여부

        String capEmail = mypageMapper.getOneCaptain(email,crewNum);

        System.out.println("조회한 캡틴 이메일 test >>>>> " + capEmail);

        //캡틴 이메일과 같은 면 삭제 불가능
        if(capEmail.equals(email)){
            Response = "none";
        }else {
            //삭제
            delcrews = mypageMapper.deleteCrew(email,crewNum);
            //삭제가 정상적으로 처리되면
            if(delcrews == 1){
                Response = "OK";
            }else{
                Response = "false";
            }
        }

        return Response;
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
    public JSONArray getOneAccount(String email, String myaccountName) throws Exception {

        //jsonarray로 return하기

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        HashMap<String,Object> hashMap = new HashMap<>();

        List<MyAccountDTO> accountLists = mypageMapper.getOneAccount(email,myaccountName); //하나의 가상계좌정보

        for(MyAccountDTO list : accountLists){
            hashMap.put("selectAname", list.getMyaccount_name());

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);
        }

        System.out.println("수정된 계좌명(서->클) test >>>>> " + jsonArray);

        return jsonArray;
    }

    @Override
    public JSONArray getUseAccounts(String accountNum, int monthNum) throws Exception {

        //jsonarray로 return하기

        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        List<OpenBankUsingDTO> useAccounts1 = mypageMapper.getUseAccounts(accountNum,monthNum);

        //month 1,3,6
        for(OpenBankUsingDTO list1 : useAccounts1){
            hashMap.put("openUseDate", list1.getOpenuse_date());
            hashMap.put("openUseContent", list1.getOpenuse_content());
            hashMap.put("openuseAssort", list1.getOpenuse_assort());
            hashMap.put("openUseIn", list1.getOpenuse_inmoney());
            hashMap.put("openUseOut", list1.getOpenuse_outmoney());

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);
        }

        System.out.println(monthNum + "개월, " + jsonArray);

        return jsonArray;
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

        //내 게시글 조회 후 조회된 게시글 중 게시글 번호를 꺼내 getCrewName에 사용
        //게시글 크루명 조회 후 myBoardLists에 set

        List<CrewBoardDTO> myBoardLists = mypageMapper.getMyboard(email,start,end); //내 게시글 조회

        //게시글 크루명 조회 후 myBoardLists 넣기
        for (CrewBoardDTO dto : myBoardLists) {
            String boardStr = mypageMapper.getCrewName(dto.getCrew_num());
            dto.setCrew_name(boardStr);
        }

        return myBoardLists;
    }


    @Override
    public void deleteMyboard(int boardNum) throws Exception {
        mypageMapper.deleteMyboard(boardNum);
    }

    @Override
    public List<CrewCommentDTO> getMyComment(String email) throws Exception {

        List<CrewCommentDTO> myCommentLists = mypageMapper.getMyComment(email); //내 댓글 조회

        for (CrewCommentDTO dto : myCommentLists){
            String commentStr = mypageService.getCrewName(dto.getCrew_num());
            String commentSubject = mypageService.getBoardSubject(dto.getBoard_num());

            dto.setCrew_name(commentStr);
            dto.setBoard_subject(commentSubject);
        }

        return myCommentLists;
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

        //totalpage를 반환

        int numPerPage = 5; //한 페이지 표시될 게시글 수
        int boardCount = mypageMapper.getBoardCount(email);
        int totalPage = myUtil.getPageCount(numPerPage,boardCount);

        return totalPage;
    }

    @Override
    public JSONArray getSchedule(String email) throws Exception {

        //jsonarray로 return하기
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        HashMap<String,Object> hashMap = new HashMap<>();

        List<ScheduleDTO> myScheLists = getSchedule(email); //로그인한 사용자의 일정들

        for (ScheduleDTO dtoList : myScheLists){
            hashMap.put("title",dtoList.getSche_title());
            hashMap.put("start",dtoList.getSche_start());
            hashMap.put("end",dtoList.getSche_end());
            hashMap.put("allDay",dtoList.getSche_alldayTF());
            hashMap.put("color",dtoList.getSche_assort());
            hashMap.put("textColor","#FFFFFF");

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public JSONArray getOneSchedule(String email, String scheStart) throws Exception {

        //jsonarray로 return하기
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        HashMap<String,Object> hashMap = new HashMap<>();

        List<ScheduleDTO> onedayScheLists = getOneSchedule(email,clickDate);

        System.out.println("하루 일정 조회 test >>>>> " + onedayScheLists);

        for (ScheduleDTO dtoList : onedayScheLists){
            int getCrewName = dtoList..getCrew_num();
            String crewName = getScheCrewName(email,getCrewName);

            dtoList.setCrew_name(crewName);

            hashMap.put("title", dtoList.getSche_title());
            hashMap.put("start", dtoList.getSche_start());
            hashMap.put("end", dtoList.getSche_end());
            hashMap.put("allDay", dtoList.getSche_alldayTF());
            hashMap.put("color", dtoList.getSche_assort());
            hashMap.put("crewName", dtoList.getCrew_name());

            jsonObject = new JSONObject(hashMap);
            jsonArray.add(jsonObject);
        }

        System.out.println("하루 일정 (서->클) test >>>>>>" + jsonArray);

        return jsonArray;
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

        List<MyAlertDTO> myAlertList = mypageMapper.getMyalert(email);

        //crewNum으로 crewName 가져와서 myAlertList에 넣기
        for(MyAlertDTO dto : myAlertList){
            String crewName = mypageMapper.getCrewName(dto.getCrew_num());
            dto.setCrew_name(crewName);
        }

        return myAlertList;
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
