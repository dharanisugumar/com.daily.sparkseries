package com.geektrust.backend.exceptions;

public class PassengerCategoryInvalidTypeException extends RuntimeException{
    public  PassengerCategoryInvalidTypeException(String msg){
        super(msg);
    }
}
