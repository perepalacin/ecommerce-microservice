package com.perepalacin.cart_service.client;

import com.perepalacin.cart_service.entity.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductServiceClient {

    @Value("${product.service.url}")
    private String productServiceUrl;

    public ResponseEntity<ProductDto> getProductById(final Long productId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(productServiceUrl + "/id/" + productId, ProductDto.class);

        return  ResponseEntity.ok().build();
    }

    public ResponseEntity<List<ProductDto>> getListOfProductsById(final List<Long> productIds) {
        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<List<ProductDto>> responseType =
                new ParameterizedTypeReference<List<ProductDto>>() {};

        HttpEntity<List<Long>> requestEntity = new HttpEntity<>(productIds);

        return restTemplate.exchange(
                productServiceUrl + "/batch",
                HttpMethod.POST,
                requestEntity,
                responseType
        );
    }

}
