package com.airbnb.exception;

public class EmptyFileException extends  RuntimeException{

    public EmptyFileException (String msg){
        super(msg);
    }
}
