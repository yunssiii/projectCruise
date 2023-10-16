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

}
