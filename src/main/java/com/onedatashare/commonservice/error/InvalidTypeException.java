package com.onedatashare.commonservice.error;

public class InvalidTypeException extends Exception{
    public InvalidTypeException(String typeExpected){
        super("Invalid type. Expected object of type " + typeExpected);
    }
}
