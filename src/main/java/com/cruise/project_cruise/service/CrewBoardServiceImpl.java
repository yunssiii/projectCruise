package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewBoardDTO;
import com.cruise.project_cruise.dto.CrewMemberDTO;
import com.cruise.project_cruise.mapper.CrewBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
public class CrewBoardServiceImpl implements CrewBoardService {

	@Autowired
	private CrewBoardMapper mapper;

	@Override
	public int maxNum() throws Exception {
		return mapper.maxNum();
	}

	@Override
	public int crewmaxNum(int crew_num) throws Exception {
		return mapper.crewMaxNum(crew_num);
	}

	@Override
	public void insertData(CrewBoardDTO dto) throws Exception {
        mapper.insertData(dto);
	}

	@Override
	public int getDataCount(String searchKey, String searchValue, int crew_num) throws Exception {
		return mapper.getDataCount(searchKey, searchValue, crew_num);
	}

	@Override
	public List<CrewBoardDTO> getLists(int start, int end,
									   String searchKey, String searchValue,
									   int crew_num, int currentPage, int totalPage) throws Exception {
		return mapper.getLists(start, end, searchKey, searchValue, crew_num,currentPage,totalPage);
	}

	@Override
	public CrewBoardDTO getReadData(int num) throws Exception {
		return mapper.getReadData(num);
	}

	@Override
	public void updateHitCount(int num) throws Exception {
        mapper.updateHitCount(num);
	}

	@Override
	public void updateData(CrewBoardDTO dto) throws Exception {
        mapper.updateData(dto);
	}

	@Override
	public void deleteData(int num) throws Exception {
        mapper.deleteData(num);
	}

	@Override
	public Map<String, Object> boardTitle(int crew_num) throws Exception {
		return mapper.boardTitle(crew_num);
	}

	@Override
	public String checkCaptain(String email, int crew_num) throws Exception {
		return mapper.checkCaptain(email, crew_num);
	}

	@Override
	public String getUserName(String email) throws Exception {
		return mapper.getUserName(email);
	}

	@Override
	public String getFileName(int board_num) throws Exception {
		return mapper.getFileName(board_num);
	}

	@Override
	public String getCrewName(int crew_num) throws Exception {
		return mapper.getCrewName(crew_num);
	}

}
