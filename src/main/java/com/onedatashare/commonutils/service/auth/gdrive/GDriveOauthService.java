/**
 * ##**************************************************************
 * ##
 * ## Copyright (C) 2018-2020, OneDataShare Team,
 * ## Department of Computer Science and Engineering,
 * ## University at Buffalo, Buffalo, NY, 14260.
 * ##
 * ## Licensed under the Apache License, Version 2.0 (the "License"); you
 * ## may not use this file except in compliance with the License.  You may
 * ## obtain a copy of the License at
 * ##
 * ##    http://www.apache.org/licenses/LICENSE-2.0
 * ##
 * ## Unless required by applicable law or agreed to in writing, software
 * ## distributed under the License is distributed on an "AS IS" BASIS,
 * ## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * ## See the License for the specific language governing permissions and
 * ## limitations under the License.
 * ##
 * ##**************************************************************
 */


package com.onedatashare.commonutils.service.auth.gdrive;


import com.google.api.client.auth.oauth2.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.onedatashare.commonutils.config.auth.GDrivePropertiesConfig;
import com.onedatashare.commonutils.error.ODSException;
import com.onedatashare.commonutils.model.credential.OAuthEndpointCredential;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Map;

@Service
public class GDriveOauthService {

    private final GDrivePropertiesConfig gDrivePropertiesConfig;

    private GoogleAuthorizationCodeFlow flow;

    private String oAuthUrl;

    private static final String CODE = "code";

    private DataStore<StoredCredential> storedCredentialDataStore;
    public GDriveOauthService(@Qualifier("GDrivePropertiesConfig") GDrivePropertiesConfig gDrivePropertiesConfig) {
        this.gDrivePropertiesConfig = gDrivePropertiesConfig;
    }
    @PostConstruct
    @SneakyThrows
    public void initialize() {
        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                gDrivePropertiesConfig.getClientSecrets(), gDrivePropertiesConfig.SCOPES)
                .setAccessType(gDrivePropertiesConfig.ACCESS_TYPE)
                .setDataStoreFactory(MemoryDataStoreFactory.getDefaultInstance())
                .setApprovalPrompt(gDrivePropertiesConfig.APPROVAL_PROMPT)
                .build();

        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(gDrivePropertiesConfig.getRedirectUri())
                .setState(flow.getClientId());
        this.oAuthUrl = authorizationUrl.toURL().toString();
    }


    public String start() {
        return this.oAuthUrl;
    }

    public OAuthEndpointCredential finish(Map<String, String> queryParameters) {
        String code = queryParameters.get(CODE);
        try {
            TokenResponse response = this.flow.newTokenRequest(code)
                    .setRedirectUri(gDrivePropertiesConfig.getRedirectUri())
                    .execute();
            Credential driveCredential = this.flow.createAndStoreCredential(response, "user");

            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            Drive service = new Drive.Builder(httpTransport, jsonFactory, driveCredential)
                    .setApplicationName(gDrivePropertiesConfig.getAppName())
                    .build();


            String userId = service.about().get().setFields("user").execute().getUser().getEmailAddress();
            Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
            calendar.add(Calendar.SECOND, Math.toIntExact(response.getExpiresInSeconds()));

            return new OAuthEndpointCredential(userId)
                    .setToken(response.getAccessToken())
                    .setRefreshToken(response.getRefreshToken())
                    .setExpiresAt(calendar.getTime())
                    .setTokenExpires(true);
        } catch (IOException | GeneralSecurityException e) {
            throw new ODSException(e.getMessage(),e.getClass().getName());
        }
    }
}

