package com.perepalacin.cart_service.client;

import com.perepalacin.cart_service.entity.dao.ProductDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceClient {

    @Value("${product.service.url}")
    private String productServiceUrl;

    public ResponseEntity<ProductDao> getProductById(final Long productId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(productServiceUrl + productId, ProductDao.class);

        return  ResponseEntity.ok().build();
    }
}
