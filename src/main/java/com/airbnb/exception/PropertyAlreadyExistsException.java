package com.airbnb.exception;

public class PropertyAlreadyExistsException extends  RuntimeException{

    public PropertyAlreadyExistsException(String msg){
        super(msg);
    }
}
