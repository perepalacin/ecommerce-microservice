package com.perepalacin.cart_service.service;

import com.perepalacin.cart_service.client.ProductServiceClient;
import com.perepalacin.cart_service.entity.dao.CartItem;
import com.perepalacin.cart_service.entity.dao.RestockAlert;
import com.perepalacin.cart_service.entity.dto.ProductDto;
import com.perepalacin.cart_service.entity.dto.RestockRequest;
import com.perepalacin.cart_service.exceptions.BadRequestException;
import com.perepalacin.cart_service.exceptions.ProductNotFoundException;
import com.perepalacin.cart_service.repository.RestockAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestockService {

    private final ProductServiceClient productServiceClient;
    private final RestockAlertRepository restockAlertRepository;

    public void createRestockRequest (final Long productId, final String email) {
        ResponseEntity<ProductDto> productResponse = productServiceClient.getProductById(productId);
        if (productResponse.getStatusCode().is2xxSuccessful()) {
            if (productResponse.getBody() != null && productResponse.getBody().getStock() > 0) {
                throw new BadRequestException("The product is still in stock, try refreshing your page. If the problem persists, please contact us.");
            }
            restockAlertRepository.save(RestockAlert.builder().productId(productId).userEmail(email).build());
        } else if (HttpStatus.NOT_FOUND.equals(productResponse.getStatusCode())) {
            throw new ProductNotFoundException("Product with id: " + productId + " not found");
        } else {
            throw new RuntimeException("Something went wrong, please try again later");
        }
    }

    public List<String> getAllUsersSubscribedToUserId (final Long productId) {
        List<RestockAlert> users = restockAlertRepository.findByProductId(productId);
        return users.stream().map(RestockAlert::getUserEmail).toList();
    }
}
