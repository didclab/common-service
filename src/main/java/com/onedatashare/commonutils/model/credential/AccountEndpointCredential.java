package com.onedatashare.commonutils.model.credential;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;


/**
 * POJO for storing account credential i.e., userName and Password
 */
@Data
public class AccountEndpointCredential extends EndpointCredential {
    private String username;
    @ToString.Exclude
    private String secret;
    private String uri;
    @JsonIgnore
    private byte[] encryptedSecret;

    public static String[] uriFormat(AccountEndpointCredential credential, EndpointCredentialType type) {
        String noTypeUri = "";
        if(type.equals(EndpointCredentialType.sftp)){
            noTypeUri = credential.getUri().replaceFirst("sftp://", "");
        }else if(type.equals(EndpointCredentialType.ftp)){
            noTypeUri = credential.getUri().replaceFirst("ftp://", "");
        }
        else{
            noTypeUri = credential.getUri().replaceFirst("http://", "");
        }
        return noTypeUri.split(":");
    }
}
