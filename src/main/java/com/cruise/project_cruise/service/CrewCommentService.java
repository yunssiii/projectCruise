package com.cruise.project_cruise.service;


import com.cruise.project_cruise.dto.CrewCommentDTO;

import java.util.List;

public interface CrewCommentService {

	public int maxNum() throws Exception;
	public void insertData(CrewCommentDTO dto) throws Exception;
	public int getDataCount(int board_num) throws Exception;
	public List<CrewCommentDTO> getLists(int board_num) throws Exception;
	public void updateData(CrewCommentDTO dto) throws Exception;
	public void deleteData(int comment_num) throws Exception;
	public void insertCommentReply(CrewCommentDTO dto) throws Exception;
}
