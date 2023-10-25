package com.cruise.project_cruise.service;


import com.cruise.project_cruise.dto.CrewBoardDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;

import java.util.List;
import java.util.Map;


public interface CrewBoardService {

	public int maxNum() throws Exception;

	public int crewmaxNum(int crew_num) throws Exception;
	public void insertData(CrewBoardDTO dto) throws Exception;
	public int getDataCount(String searchKey, String searchValue,
							int crew_num) throws Exception;
	public List<CrewBoardDTO> getLists(int start, int end,
									   String searchKey, String searchValue,
									   int crew_num, int currentPage, int totalPage) throws Exception;
	public CrewBoardDTO getReadData(int num) throws Exception;
	public void updateHitCount(int num) throws Exception;
	public void updateData(CrewBoardDTO dto) throws Exception;
	public void deleteData(int num) throws Exception;
	public Map<String, Object> boardTitle(int crew_num) throws Exception;
	public String checkCaptain(String email, int crew_num) throws Exception;
	public String getUserName(String email) throws Exception;
	public String getFileName(int board_num) throws Exception;
	public String getCrewName(int crew_num) throws Exception;
}
