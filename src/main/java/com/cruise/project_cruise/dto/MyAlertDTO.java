package com.cruise.project_cruise.dto;

import lombok.Data;

@Data
public class MyAlertDTO {

    private int myalert_num;
    private int crew_num;
    private String email;
    private String myalert_assort; //알림 분류
    private String myalert_content; //알림 내용
    private String myalert_adate; //알림일

    //-- 내 알림에서 링크 연결에 필요
    private int board_num;
    private String crew_name;


}
