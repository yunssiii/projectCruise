package com.cruise.project_cruise.dto;

import lombok.Data;

@Data
public class CrewMemberDTO {

    /*
    * 만들어야 할 컬럼들
    * create table crew_member (
        crew_num number(10),
        cmem_email varchar2(255),
        captain_YN char(1) default('N') constraint ck_cmember_captainYN check(captain_YN in ('Y','N')),
        join_date date default(sysdate),
        total_pay number(10) default 0,
        must_paycount number(10) default 0,
        real_paycount number(10) default 0,
        constraint pk_cmember_num primary key(crew_num,cmem_email),
        constraint fk_cmember_num foreign key(crew_num) references crew (crew_num) on delete cascade,
        constraint fk_cmember_email foreign key(cmem_email) references users (email) on delete cascade
        );
    *
    * */

    private int cmem_num;
    private int crew_num;
    private String cmem_email;
    private String captain_YN;
    private String join_date; // 가입일자
    private int total_pay; // 해당 선원이 모임에 납입한 총 금액
    private int must_paycount; // 납입 의무횟수
    private int real_paycount; // 실제 납입횟수

}
