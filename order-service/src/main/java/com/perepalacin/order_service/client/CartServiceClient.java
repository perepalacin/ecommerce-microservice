package com.perepalacin.order_service.client;

import com.perepalacin.order_service.entity.dto.CartDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class CartServiceClient {

    @Value("${cart.service.url}")
    private String cartServiceUrl;

    public ResponseEntity<CartDto> getCartByUserId(final UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(cartServiceUrl + userId, CartDto.class);
        return  ResponseEntity.ok().build();
    }

}
