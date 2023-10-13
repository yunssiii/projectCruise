package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.UserDTO;
import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageMapper {

    public List<CrewDTO> getCrews(String email) throws Exception;
    public void deleteCrew(@Param("cmem_email")String email, @Param("crew_num") int crewNum) throws Exception;
    public  List<CrewMemberDTO> getCrewNums(String email) throws Exception;
    public List<OpenBankDTO> getOpenAccPWd(String email) throws Exception;
    public void insertAccount(@Param("email")String email,@Param("myaccount_anum") String myaccountAnum) throws Exception;
    public  List<OpenBankDTO> getAccounts(String email) throws Exception;
    public String getWebpassword(String email) throws Exception;
    public void updateWebpassword(@Param("pay_password") String payPwd, @Param("email")String email) throws Exception;
    public UserDTO getUserName(String email) throws Exception;
    public UserDTO getUserInfo(String email) throws Exception;
}
