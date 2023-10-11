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
}
