package com.onedatashare.commonutils.model.credential;

import com.onedatashare.commonutils.error.InvalidTypeException;
import lombok.Data;
import lombok.SneakyThrows;


/**
 * Base class for storing one user credential
 */
@Data
public class EndpointCredential extends Object implements Cloneable{
    protected String accountId;

    public EndpointCredential(String accountId){
        this.accountId = accountId;
    }

    public EndpointCredential(){}

    public static AccountEndpointCredential getAccountCredential(EndpointCredential endpointCredential){
        if(endpointCredential instanceof  AccountEndpointCredential){
            return (AccountEndpointCredential) endpointCredential;
        }else{
            return null;
        }
    }
    public static OAuthEndpointCredential getOAuthCredential(EndpointCredential endpointCredential){
        if(endpointCredential instanceof OAuthEndpointCredential){
            return (OAuthEndpointCredential) endpointCredential;
        }else{
            return null;
        }
    }

    @SneakyThrows
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EndpointCredential)) {
            throw new InvalidTypeException(EndpointCredential.class.toString());
        }
        return accountId.toLowerCase().equals(((EndpointCredential) obj).accountId.toLowerCase());
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}