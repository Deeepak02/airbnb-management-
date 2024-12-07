package com.airbnb.exception;

public class PropertyNotFound extends  RuntimeException{

    public PropertyNotFound (String msg){
        super(msg);
    }
}
