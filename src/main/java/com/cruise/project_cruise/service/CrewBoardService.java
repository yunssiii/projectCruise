package com.cruise.project_cruise.service;


import com.cruise.project_cruise.dto.CrewBoardDTO;

import java.util.List;
import java.util.Map;


public interface CrewBoardService {

	public int maxNum() throws Exception;
	public void insertData(CrewBoardDTO dto) throws Exception;
	public int getDataCount(String searchKey, String searchValue,
							int crew_num) throws Exception;
	public List<CrewBoardDTO> getLists(int start, int end,
									   String searchKey, String searchValue,
									   int crew_num) throws Exception;
	public CrewBoardDTO getReadData(int num) throws Exception;
	public void updateHitCount(int num) throws Exception;
	public void updateData(CrewBoardDTO dto) throws Exception;
	public void deleteData(int num) throws Exception;
	public Map<String, Object> boardTitle(int crew_num) throws Exception;
	public String checkCaptain(String email) throws Exception;
}
