package com.onedatashare.commonutils.config.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties
public class CredentialServicePropertiesConfig {

    @Value("${cred.service.uri: http://EndpointCredentialService/v1/endpoint-cred}")
    private String credentialServiceUrl;
}
