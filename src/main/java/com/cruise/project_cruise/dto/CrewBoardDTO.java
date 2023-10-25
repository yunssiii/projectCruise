package com.cruise.project_cruise.dto;

import lombok.Data;

@Data
public class CrewBoardDTO {

	private int crew_num;
	private int board_num;
	private String email;
	private String name;
	private String board_subject;
	private String board_content;
	private String board_created;
	private int hitCount;

	private int rnum;
	private int comment_count;
	private int notice;
	private String savedFile;

	// 마이페이지 게시글 조회 시 뿌려줄 데이터
	private String crew_name;
	private String board_created_tochar;
}
