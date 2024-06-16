package com.onedatashare.commonutils.config.auth;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix="dropbox")
public class DbxPropertiesConfig {

    @Value("${dropbox.key:dummyKey}")
    private String key;

    @Value("${dropbox.secret:dummySecret}")
    private String secret;

    @Value("${dropbox.redirectUri:/api/oauth/dropbox}")
    private String redirectUri;

    @Value("${dropbox.identifier:OneDataShare-DIDCLab}")
    private String identifier;
}
