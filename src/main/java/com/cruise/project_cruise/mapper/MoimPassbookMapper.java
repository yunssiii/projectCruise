package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.CrewDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.dto.MyAccountDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MoimPassbookMapper {

	public int maxCrewNum() throws Exception;
	public void insertCrew(CrewDTO dto) throws Exception;
	public void insertCrewMember(CrewMemberDTO dto) throws Exception;
	public List<MyAccountDTO> getMyAccount(String email) throws Exception;
	public String getBankName(String myaccount_anum) throws Exception;
	public int maxCmemNum() throws Exception;

}

