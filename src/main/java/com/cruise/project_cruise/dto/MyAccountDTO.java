package com.cruise.project_cruise.dto;

import lombok.Data;

@Data
public class MyAccountDTO {

    private String email;
    private String myaccount_bank;
    private String myaccount_anum;
    private String myaccount_anum2; //계좌번호 가려진 거
    private String myaccount_date;
    private String myaccount_name;

    private int myaccount_balance;

}
