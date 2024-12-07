package com.airbnb.exception;

public class RoomEmptyException extends  RuntimeException{
    public RoomEmptyException (String msg){
        super(msg);
    }
}
