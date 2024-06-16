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


package com.onedatashare.commonutils.service.auth.box;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxUser;
import com.onedatashare.commonutils.config.auth.BoxPropertiesConfig;
import com.onedatashare.commonutils.model.credential.OAuthEndpointCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;

@Service
public class BoxOauthService{

    @Autowired
    BoxPropertiesConfig boxPropertiesConfig;
    Logger logger = LoggerFactory.getLogger(BoxOauthService.class);

    public String start() {
        String box_redirect = boxPropertiesConfig.getAuthUri()
                + "?response_type=code"
                + "&client_id=" + boxPropertiesConfig.getClientId()
                + "&redirect_uri=" + boxPropertiesConfig.getRedirectUri()
                + "&scope=" + boxPropertiesConfig.getScope();

        return box_redirect;
    }

    /**
     * @param queryParameters: Access Token returned by Box Authentication using OAuth 2
     * @return OAuthCredential
     */

    public OAuthEndpointCredential finish(Map<String, String> queryParameters) {
        String code = queryParameters.get("code");
        // Instantiate new Box API connection object
        BoxAPIConnection client = new BoxAPIConnection(boxPropertiesConfig.getClientId(), boxPropertiesConfig.getClientSecret(), code);
        client.refresh();
        BoxUser user = BoxUser.getCurrentUser(client);
        BoxUser.Info userInfo = user.getInfo();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, Math.toIntExact(client.getExpires()));

        return new OAuthEndpointCredential(userInfo.getLogin())
                .setToken(client.getAccessToken())
                .setTokenExpires(true)
                .setRefreshToken(client.getRefreshToken())
                .setRefreshTokenExpires(true)
                .setExpiresAt(calendar.getTime());

    }
}
