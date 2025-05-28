package com.perepalacin.auth_service.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Builder
public class KeycloakTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("refresh_expires_ins")
    private Long refreshExpiresIn;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("session_state")
    private String sessionState;
    private String scope;
}
