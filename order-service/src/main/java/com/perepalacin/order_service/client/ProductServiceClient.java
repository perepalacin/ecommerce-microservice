package com.perepalacin.order_service.client;

import com.perepalacin.order_service.entity.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class ProductServiceClient {

    @Value("${product.service.url}")
    private String productServiceUrl;

    public ResponseEntity<ProductDto> getProductById(final Long productId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(productServiceUrl + "/id/" + productId, ProductDto.class);
        return ResponseEntity.ok().build();
    }
}
