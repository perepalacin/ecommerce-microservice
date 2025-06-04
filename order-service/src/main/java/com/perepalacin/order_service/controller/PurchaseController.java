package com.perepalacin.order_service.controller;

import com.perepalacin.order_service.entity.dto.PurchaseDto;
import com.perepalacin.order_service.request.PurchaseRequest;
import com.perepalacin.order_service.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createPurchase(purchaseRequest));
    }

    @PatchMapping("/billing/{purchaseId}")
    public ResponseEntity<PurchaseDto> editBillingAddressPurchase (@PathVariable Long purchaseId, @RequestParam Long billingAddressId) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.editPurchaseAddress(purchaseId, billingAddressId, true));
    }

    @PatchMapping("/delivery/{purchaseId}")
    public ResponseEntity<PurchaseDto> editDeliveryAddressPurchase (@PathVariable Long purchaseId, @RequestParam Long deliveryAddressId) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.editPurchaseAddress(purchaseId, deliveryAddressId, false));
    }

    @PatchMapping("/item-quantity/{purchaseId}")
    public ResponseEntity<PurchaseDto> editPurchaseItemQuantity (@PathVariable Long purchaseId, @RequestParam Long itemId, @RequestParam Integer newQuantity) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.editPurchaseItemQuantity(purchaseId, itemId, newQuantity));
    }

    @PatchMapping("/remove-item/{purchaseId}")
    public ResponseEntity<PurchaseDto> removePurchaseITem (@PathVariable Long purchaseId, @RequestParam Long itemId) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.removePurchaseItem(purchaseId, itemId));
    }

    @PatchMapping("/cancel/{purchaseId}")
    public ResponseEntity<Void> cancelPurchase (@PathVariable Long purchaseId) {
        purchaseService.cancelPurchase(purchaseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
