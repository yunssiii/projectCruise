package com.cruise.project_cruise.dto.develop;

import lombok.Data;

@Data
public class OpenBankDTO {
    private String open_account;
    private String open_bank;
    private String open_aname; // 계좌상품명
    private String open_name; // 명의
    private String open_password;
    private int open_balance;
}
