package com.cruise.project_cruise.mapper;


import com.cruise.project_cruise.dto.CrewBoardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CrewBoardMapper {

	public int maxNum() throws Exception;
	public void insertData(CrewBoardDTO dto) throws Exception;
	public int getDataCount(@Param("searchKey") String searchKey,
							@Param("searchValue") String searchValue,
							@Param("crew_num") int crew_num) throws Exception;
	public List<CrewBoardDTO> getLists(@Param("start") int start, @Param("end") int end,
								   @Param("searchKey") String searchKey,
								   @Param("searchValue") String searchValue,
								   @Param("crew_num") int crew_num) throws Exception;
	public CrewBoardDTO getReadData(int num) throws Exception;
	public void updateHitCount(int num) throws Exception;
	public void updateData(CrewBoardDTO dto) throws Exception;
	public void deleteData(int num) throws Exception;
}

