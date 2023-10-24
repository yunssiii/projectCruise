package com.cruise.project_cruise.exception;

public class TransferMoneyZeroException extends Exception {
    public TransferMoneyZeroException() {}
    public TransferMoneyZeroException(String errorMsg) {
        super(errorMsg);
    }
}
