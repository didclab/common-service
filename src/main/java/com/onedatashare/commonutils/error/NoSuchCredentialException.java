package com.onedatashare.commonutils.error;

import com.onedatashare.commonutils.model.credential.EndpointCredentialType;

public class NoSuchCredentialException extends Exception{
    public NoSuchCredentialException(EndpointCredentialType type, String credId){
        super("No such credential found :" + type + "/" + credId);
    }
}
