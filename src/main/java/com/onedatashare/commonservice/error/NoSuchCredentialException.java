package com.onedatashare.commonservice.error;

import com.onedatashare.commonservice.model.credential.EndpointCredentialType;

public class NoSuchCredentialException extends Exception{
    public NoSuchCredentialException(EndpointCredentialType type, String credId){
        super("No such credential found :" + type + "/" + credId);
    }
}
