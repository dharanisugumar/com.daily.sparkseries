package com.geektrust.backend.exceptions;

public class PassengerTravelTypeInvalidTypeException extends RuntimeException{
    public PassengerTravelTypeInvalidTypeException(String msgStr){
        super(msgStr);
    }
}