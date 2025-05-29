package com.perepalacin.auth_service.controller;

import com.perepalacin.auth_service.entity.dao.AddressDao;
import com.perepalacin.auth_service.request.AddressRequest;
import com.perepalacin.auth_service.service.AddressesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressesService addressesService;

    @GetMapping
    public ResponseEntity<List<AddressDao>> getUserAddresses () {
        return ResponseEntity.ok(addressesService.getUserAddresses());
    }

    @PostMapping
    public ResponseEntity<AddressDao> createNewAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressesService.addAddress(addressRequest));
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<AddressDao> editAddress (@PathVariable Long addressId, @RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(addressesService.editAddress(addressId, addressRequest));
    }

    @PatchMapping("/change-default/{addressId}")
    public ResponseEntity<Void> editDefaultAddress (@PathVariable Long addressId) {
        addressesService.editDefaultAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress (@PathVariable Long addressId) {
        addressesService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
