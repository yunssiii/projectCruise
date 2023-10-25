package com.cruise.project_cruise.mapper;


import com.cruise.project_cruise.dto.CrewBoardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CrewBoardMapper {

	public int maxNum() throws Exception;
	public int crewMaxNum(int crew_num) throws Exception;
	public void insertData(CrewBoardDTO dto) throws Exception;
	public int getDataCount(@Param("searchKey") String searchKey,
							@Param("searchValue") String searchValue,
							@Param("crew_num") int crew_num) throws Exception;
	public List<CrewBoardDTO> getLists(@Param("start") int start, @Param("end") int end,
                                       @Param("searchKey") String searchKey,
                                       @Param("searchValue") String searchValue,
                                       @Param("crew_num") int crew_num, int currentPage, int totalPage) throws Exception;
	public CrewBoardDTO getReadData(int num) throws Exception;
	public void updateHitCount(int num) throws Exception;
	public void updateData(CrewBoardDTO dto) throws Exception;
	public void deleteData(int num) throws Exception;
	public Map<String, Object> boardTitle(int crew_num) throws Exception;
	public String checkCaptain(String email, int crew_num) throws Exception;
	public String getUserName(String email) throws Exception;
}

