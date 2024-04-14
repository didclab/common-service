package com.onedatashare.commonservice.model.credential;

import com.google.gson.annotations.JsonAdapter;
import com.onedatashare.commonservice.config.EpochMillisDateAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * POJO for storing OAuth Credentials
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
public class OAuthEndpointCredential extends EndpointCredential {
    @ToString.Exclude
    private String token;
    private byte[] encryptedToken;
    private boolean tokenExpires = false;
    @JsonAdapter(EpochMillisDateAdapter.class)
    private Date expiresAt;
    private String refreshToken;
    private byte[] encryptedRefreshToken;
    private boolean refreshTokenExpires = false;

    public OAuthEndpointCredential(String id){
        super(id);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}