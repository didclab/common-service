package com.onedatashare.commonutils.model.credential;

public enum AccountCredentialType {
    s3, sftp, http, ftp, scp;
    
    public static boolean contains(String name) {
        for (AccountCredentialType type : values()) {
            if (type.name().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
    
}
