package com.duongw.stayeasy.exception;

public class AlreadyExistedException extends RuntimeException {
    public AlreadyExistedException(String message){
        super(message);
    }

}
