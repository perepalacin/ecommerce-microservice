package com.perepalacin.product_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RestockServiceClient {

    @Value("${cart.service.url}")
    private String cartServiceUrl;
    @Value("${cart.service.password}")
    private String cartServicePassword;

    public ResponseEntity<List<String>> getListOfUsersSubscribedToProductRestock(final Long productId) {
        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<List<String>> responseType =
                new ParameterizedTypeReference<>() {
                };

        HttpEntity<Long> requestEntity = new HttpEntity<>(productId);

        return restTemplate.exchange(
                cartServiceUrl + "/api/v1/stock/all/" + productId + "?password=" + cartServicePassword,
                HttpMethod.POST,
                requestEntity,
                responseType
        );
    }
}
