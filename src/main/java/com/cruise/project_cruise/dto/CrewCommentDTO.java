package com.cruise.project_cruise.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CrewCommentDTO {

	private int comment_num;
	private int crew_num;
	private int board_num;
	private String email;
	private String name;
	private String comment_content;
	private String comment_created;
	private int ref_no;
	private int ref_level;

	// 마이페이지 게시글 조회 시 뿌려줄 데이터
	private String crew_name;
	private String comment_created_tochar;
	private String board_subject;

}
