package com.cruise.project_cruise.service;

import com.cruise.project_cruise.dto.TemplateDTO;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import com.cruise.project_cruise.mapper.DevelopOpenBankUsingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevelopOpenBankUsingServiceImpl implements DevelopOpenBankUsingService {

    @Autowired
    private DevelopOpenBankUsingMapper developOpenBankUsingMapper;

    @Override
    public List<OpenBankUsingDTO> getUsingList(String selectedAccount) throws Exception {
        return developOpenBankUsingMapper.getUsingList(selectedAccount);
    }

    @Override
    public void insertUsing(OpenBankUsingDTO openBankUsingDTO) throws Exception {
        developOpenBankUsingMapper.insertUsing(openBankUsingDTO);
    }

    @Override
    public int getUsingMaxNum() throws Exception {
        return developOpenBankUsingMapper.getUsingMaxNum();
    }

    @Override
    public int getBalance(int openUseNum, String selectedAccount) {
        return developOpenBankUsingMapper.getBalance(openUseNum, selectedAccount);
    }

    @Override
    public int getAccountMaxNum(String selectedAccount) throws Exception {
        return developOpenBankUsingMapper.getAccountMaxNum(selectedAccount);
    }

    @Override
    public String getLastDate(String selectedAccount, int selectedNum) throws Exception {
        return developOpenBankUsingMapper.getLastDate(selectedAccount, selectedNum);
    }

    @Override
    public void updateUsing(OpenBankUsingDTO openBankUsingDTO) throws Exception {
        developOpenBankUsingMapper.updateUsing(openBankUsingDTO);
    }

    @Override
    public void deleteUsing(int openUseNum) throws Exception {
        developOpenBankUsingMapper.deleteUsing(openUseNum);
    }

    @Override
    public void updateAccountTableBalance(int openBalance, String openAccount) throws Exception {
        developOpenBankUsingMapper.updateAccountTableBalance(openBalance,openAccount);
    }
}
