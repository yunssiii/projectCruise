package com.cruise.project_cruise.dto;

import lombok.Data;

@Data
public class ScheduleDTO {

    private int sche_num; // 스케줄 고유번호
    private int crew_num; // crew 테이블 참조
    private String sche_title; // 스케줄 제목
    private String sche_assort; // 스케줄 분류
    private String sche_start; // 스케줄 시작일
    private String sche_end; // 스케줄 종료일
    private String sche_alldayTF; // 스케줄 장소

    //일정 하루 조회에 뿌리기 위해 추가
    private String crew_name;

}
