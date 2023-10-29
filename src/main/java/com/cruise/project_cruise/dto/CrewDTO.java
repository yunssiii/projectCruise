package com.cruise.project_cruise.dto;

import lombok.Data;

@Data
public class CrewDTO {

    /* 만들어야 할 컬럼들
     * create table CREW(
        crew_num number(10) constraint pk_crew_num primary key,
        crew_name varchar2(32) NOT NULL,
        crew_info varchar2(64),
        captain_email varchar2(255) REFERENCES users(email),
        crew_created date NOT NULL,
        crew_accountid varchar2(500) not null,
        crew_paydate number(3) NOT NULL constraint ck_crew_paydate check(crew_paydate between 1 and 31),
        crew_paymoney number(10) NOT NULL,
        crew_goal number(10) NOT NULL)
    *
    * */

    private int crew_num;
    private String crew_name;
    private String crew_info;
    private String captain_email;
    private String crew_created; // 크루 생성일자
    private String crew_bank; // 크루 생성일자
    private String crew_accountid; // 크루 대표 계좌번호
    private int crew_paydate; // 크루 납입일자
    private int crew_paymoney; // 크루 납입액
    private Integer crew_goal; // 크루 목표금액 - 없으면 0으로 설정되게
    private String crew_deldate; // 크루 항해중단일자

    private String crew_accountid2; //대표 계좌번호 가려진 거

}
