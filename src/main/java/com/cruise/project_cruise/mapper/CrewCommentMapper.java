package com.cruise.project_cruise.mapper;


import com.cruise.project_cruise.dto.CrewCommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrewCommentMapper {

	public int maxNum() throws Exception;
	public void insertData(CrewCommentDTO dto) throws Exception;
	public int getDataCount(int board_num) throws Exception;
	public List<CrewCommentDTO> getLists(int board_num) throws Exception;
	public void updateData(CrewCommentDTO dto) throws Exception;
	public void deleteData(int comment_num) throws Exception;
	public void insertCommentReply(CrewCommentDTO dto) throws Exception;
}

