package com.perepalacin.cart_service.controller;

import com.perepalacin.cart_service.entity.dto.RestockRequest;
import com.perepalacin.cart_service.service.RestockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class RestockController {

    private final RestockService restockService;

    @PostMapping("/{productId}")
    public ResponseEntity<Void> requestRestockEmail (@PathVariable Long productId, @RequestBody RestockRequest restockRequest) {
        restockService.createRestockRequest(productId, restockRequest.getEmail());
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/all/{productId}")
    public ResponseEntity<List<String>> getListOfUsersSubscribedToProductRestock (@PathVariable Long productId, @RequestParam String password) {
        if (!"thisendpointpassword".equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<String> emailsList = restockService.getAllUsersSubscribedToUserId(productId);
        return ResponseEntity.ok(emailsList);
    }
}
