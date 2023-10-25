package com.cruise.project_cruise.dto;

import lombok.Data;

@Data
public class CrewAlertDTO {

    private int calert_num;
    private int crew_num;
    private String calert_assort; //알림 분류
    private String calert_content; //알림 내용
    private String calert_alertdate; //알림일

}
