package com.osu.vbap.projectvbap.exception;

public class LoanAlreadyReturnedException extends RuntimeException{
    public LoanAlreadyReturnedException(String message) {
        super(message);
    }
}
