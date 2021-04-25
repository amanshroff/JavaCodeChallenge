package com.db.awmd.challenge.exception;

public class InsufficientBalanceManagerException extends RuntimeException {

     private InsufficientBalanceManagerException(String format, Object... parameters) {
         super(String.format(format, parameters));
    }

    public static InsufficientBalanceManagerException to(String format, Object... parameters) {
        return new InsufficientBalanceManagerException(format, parameters);
    }
}
