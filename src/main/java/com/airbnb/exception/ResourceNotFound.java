package com.airbnb.exception;



public class ResourceNotFound extends  RuntimeException{

    public ResourceNotFound(String msg){

        super(msg);
    }
}

