package com.perepalacin.order_service.controller;

import com.perepalacin.order_service.entity.dto.PurchaseDto;
import com.perepalacin.order_service.request.PurchaseRequest;
import com.perepalacin.order_service.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping()
    public ResponseEntity<List<PurchaseDto>> getAllUserOrders () {
        return ResponseEntity.ok(purchaseService.getAllUserOrders());
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDto> getOrderById (@PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseService.getById(purchaseId));
    }

    @PostMapping("/checkout")
    public ResponseEntity<PurchaseDto> createPurchase (@RequestBody PurchaseRequest purchaseRequest) {
        return ResponseEntity.ok(purchaseService.createPurchase(purchaseRequest));
    }
//
//    @PatchMapping("/{purchaseId}")
//    public ResponseEntity<PurchaseDto> editPurchase (@PathVariable Long purchaseId) {
//
//    }
//
    @PatchMapping("/cancel/{purchaseId}")
    public ResponseEntity<Void> cancelPurchase (@PathVariable Long purchaseId) {
        purchaseService.cancelPurchase(purchaseId);
        return ResponseEntity.ok(null);
    }

}
