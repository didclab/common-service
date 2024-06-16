package com.onedatashare.commonutils.config.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix="box")
public class BoxPropertiesConfig {

    @Value("${box.clientId:}")
    private String clientId;

    @Value("${box.clientSecret:}")
    private String clientSecret;

    @Value("${box.redirectUri:/api/oauth/box}")
    private String redirectUri;

    @Value("${box.scope:root_readwrite}")
    private String scope;

    @Value("${box.authUri:https://account.box.com/api/oauth2/authorize}")
    private String authUri;
}

