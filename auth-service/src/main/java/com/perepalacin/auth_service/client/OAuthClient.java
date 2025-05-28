package com.perepalacin.auth_service.client;


import com.perepalacin.auth_service.request.OAuthRequestEntity;
import com.perepalacin.auth_service.response.KeycloakTokenResponse;
import com.perepalacin.auth_service.entity.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OAuthClient {

    @Value("${oauth2.service.url}")
    private String oauthServiceUrl;

    @Value("${oauth2.client.id}")
    private String oauthClientId;
    @Value("${oauth2.client.secret}")
    private String oauthClientSecret;

    public KeycloakTokenResponse authenticateUser(final UserDto userDto) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("username", userDto.getEmail());
        formParams.add("password", userDto.getPassword());
        formParams.add("client_id", oauthClientId);
        formParams.add("client_secret", oauthClientSecret);
        formParams.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);

        ResponseEntity<KeycloakTokenResponse> responseEntity = restTemplate.postForEntity(oauthServiceUrl, request, KeycloakTokenResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("User authenticated successfully: " + responseEntity.getBody());
            return responseEntity.getBody();
        } else {
            log.error("Failed to authenticate the user: {}", responseEntity.getStatusCode());
            throw new RuntimeException("Authentication failed status: " + responseEntity.getStatusCode());
        }
    }

    public KeycloakTokenResponse refreshAccessToken(final String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("client_id", oauthClientId);
        formParams.add("client_secret", oauthClientSecret);
        formParams.add("grant_type", "refresh_token");
        formParams.add("refresh_token", refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);

        ResponseEntity<KeycloakTokenResponse> responseEntity = restTemplate.postForEntity(oauthServiceUrl, request, KeycloakTokenResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("Access token refreshed successfully: " + responseEntity.getBody());
            return responseEntity.getBody();
        } else {
            log.error("Failed to refresh the access token: {}", responseEntity.getStatusCode());
            throw new RuntimeException("Access token refresh failed status: " + responseEntity.getStatusCode());
        }
    }

}