/**
 ##**************************************************************
 ##
 ## Copyright (C) 2018-2020, OneDataShare Team, 
 ## Department of Computer Science and Engineering,
 ## University at Buffalo, Buffalo, NY, 14260.
 ## 
 ## Licensed under the Apache License, Version 2.0 (the "License"); you
 ## may not use this file except in compliance with the License.  You may
 ## obtain a copy of the License at
 ## 
 ##    http://www.apache.org/licenses/LICENSE-2.0
 ## 
 ## Unless required by applicable law or agreed to in writing, software
 ## distributed under the License is distributed on an "AS IS" BASIS,
 ## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ## See the License for the specific language governing permissions and
 ## limitations under the License.
 ##
 ##**************************************************************
 */


package com.onedatashare.commonutils.config.auth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.onedatashare.commonutils.model.credential.OAuthEndpointCredential;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix="gdrive")
public class GDrivePropertiesConfig {

    @Value("${gdrive.appName:OneDataShare}")
    private String appName;

    @Value("${gdrive.authUri:https://accounts.google.com/o/oauth2/auth}")
    private String authUri;

    @Value("${gdrive.tokenUri:https://accounts.google.com/o/oauth2/token}")
    private String tokenUri;

    @Value("${gdrive.authProviderUri:https://www.googleapis.com/oauth2/v1/certs}")
    private String authProviderUri;

    @Value("${gdrive.redirectUri:/api/oauth/gdrive}")
    private String redirectUri;

    @Value("${gdrive.clientId:}")
    private String clientId;

    @Value("${gdrive.clientSecret:}")
    private String clientSecret;

    @Value("${gdrive.projectId:}")
    private String projectId;

    private GoogleClientSecrets clientSecrets;
    private GoogleAuthorizationCodeFlow flow;


    private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    private HttpTransport httpTransport;

//    public final static List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE);
    public final static List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);
    public final static String ACCESS_TYPE = "offline";
    public final static String APPROVAL_PROMPT = "force";

//    private static String getValueFromResourceString(String str){
//
//        if(str == null){
//            return null;
//        }
//        //Env variable
//        else if(str.startsWith("$")){
//            if(str.endsWith("}")) {
//                str = str.substring(2, str.length() - 1);
//                return System.getenv(str);
//            }
//            else {
//                ResourceBundle resource = ResourceBundle.getBundle("application");
//                StringBuilder stringBuilder = new StringBuilder();
//                for (String s: str.split("}")){
//                    System.out.println("s");
//                }
//            }
//        }
//        //Just value
//        else{
//            return str;
//        }
//        return null;
//    }

    private GDrivePropertiesConfig(boolean b){
//        ResourceBundle resource = ResourceBundle.getBundle("application");
//        this.appName = getValueFromResourceString(resource.getString("gdrive.appName"));
//        this.authUri = getValueFromResourceString(resource.getString("gdrive.authUri"));
//        this.tokenUri = getValueFromResourceString(resource.getString("gdrive.tokenUri"));
//        this.authProviderX509CertUrl = getValueFromResourceString(resource.getString("gdrive.authUri"));
//        this.redirectUri = getValueFromResourceString(resource.getString("redirect.uri.string")) + "/api/oauth/gdrive";
//        this.clientId = getValueFromResourceString(resource.getString("gdrive.clientId"));
//        this.clientSecret = getValueFromResourceString(resource.getString("gdrive.clientSecret"));
//        this.projectId = getValueFromResourceString(resource.getString("gdrive.projectId"));

        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details()
                .setAuthUri(authUri)
                .setRedirectUris(Arrays.asList(redirectUri))
                .setTokenUri(tokenUri)
                .setClientId(clientId)
                .setClientSecret(clientSecret);

        clientSecrets = new GoogleClientSecrets()
                .setInstalled(details);

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, jsonFactory, clientSecrets, SCOPES)
                    .setAccessType(ACCESS_TYPE)
                    .setApprovalPrompt(APPROVAL_PROMPT)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public GDrivePropertiesConfig(){}

    public static GDrivePropertiesConfig getInstance(){
        //Overloading the constructor
        return new GDrivePropertiesConfig(false);
    }

    @PostConstruct
    public void initialize() {
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details()
                .setAuthUri(authUri)
                .setRedirectUris(Arrays.asList(redirectUri))
                .setTokenUri(tokenUri)
                .setClientId(clientId)
                .setClientSecret(clientSecret);

        clientSecrets = new GoogleClientSecrets()
                .setInstalled(details);

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, jsonFactory, clientSecrets, SCOPES)
                    .setApprovalPrompt(APPROVAL_PROMPT)
                    .setAccessType(ACCESS_TYPE)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) {
                try {
                    requestInitializer.initialize(httpRequest);
                    httpRequest.setConnectTimeout(3 * 60000);  // 3 minutes connect timeout
                    httpRequest.setReadTimeout(3 * 60000);  // 3 minutes read timeout
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
    }

    public Drive getDriveService(OAuthEndpointCredential credential) {
        GoogleTokenResponse googleTokenResponse = new GoogleTokenResponse()
                .setAccessToken(credential.getToken())
                .setRefreshToken(credential.getRefreshToken());
        Credential cred = null;
        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, SCOPES)
                .setApprovalPrompt(APPROVAL_PROMPT)
                .setAccessType(ACCESS_TYPE)
                .build();
        try {
            cred = this.flow.createAndStoreCredential(googleTokenResponse, String.valueOf(UUID.randomUUID()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Drive.Builder(
                this.getHttpTransport(), this.getJsonFactory(), setHttpTimeout(cred))
                .setApplicationName(this.getAppName())
                .build();
    }

}