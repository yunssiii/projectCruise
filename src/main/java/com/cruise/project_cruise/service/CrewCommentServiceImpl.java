package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.CrewCommentDTO;
import com.cruise.project_cruise.mapper.CrewCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CrewCommentServiceImpl implements CrewCommentService {

	@Autowired
	private CrewCommentMapper mapper;

	@Override
	public int maxNum() throws Exception {
		return mapper.maxNum();
	}

	@Override
	public void insertData(CrewCommentDTO dto) throws Exception {
		mapper.insertData(dto);
	}

	@Override
	public int getDataCount(int board_num) throws Exception {
		return mapper.getDataCount(board_num);
	}

	@Override
	public List<CrewCommentDTO> getLists(int board_num) throws Exception {
		return mapper.getLists(board_num);
	}

	@Override
	public void updateData(CrewCommentDTO dto) throws Exception {
		mapper.updateData(dto);
	}

	@Override
	public void deleteData(int comment_num) throws Exception {
		mapper.deleteData(comment_num);
	}

	@Override
	public void insertCommentReply(CrewCommentDTO dto) throws Exception {
		mapper.insertCommentReply(dto);
	}
}
