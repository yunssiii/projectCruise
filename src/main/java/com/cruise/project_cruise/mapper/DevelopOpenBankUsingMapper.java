package com.cruise.project_cruise.mapper;

import com.cruise.project_cruise.dto.develop.OpenBankDTO;
import com.cruise.project_cruise.dto.develop.OpenBankUsingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DevelopOpenBankUsingMapper {
    public List<OpenBankUsingDTO> getUsingList(@Param("selectedAccount") String selectedAccount) throws Exception;
    public void insertUsing(OpenBankUsingDTO openBankUsingDTO) throws Exception;
    public void updateUsing(OpenBankUsingDTO openBankUsingDTO) throws Exception;
    public void updateAccountTableBalance(@Param("open_balance") int openBalance, @Param("open_account") String openAccount) throws Exception;
    public void deleteUsing(@Param("openuse_num") int openUseNum) throws Exception;
    public int getUsingMaxNum() throws Exception;
    public int getBalance(@Param("openuse_num") int openUseNum, @Param("open_account") String selectedAccount);
    public int getAccountMaxNum(@Param("open_account") String selectedAccount) throws Exception;
    public String getLastDate(@Param("open_account") String selectedAccount, @Param("openuse_num") int selectedNum) throws Exception;


}
