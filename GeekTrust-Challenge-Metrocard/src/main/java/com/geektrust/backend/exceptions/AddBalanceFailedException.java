package com.geektrust.backend.exceptions;

public class AddBalanceFailedException extends RuntimeException{
    public AddBalanceFailedException(String msg){
        super(msg);
    }
}
