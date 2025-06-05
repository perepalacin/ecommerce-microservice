package com.perepalacin.order_service.service;

import com.perepalacin.order_service.client.AddressServiceClient;
import com.perepalacin.order_service.client.CartServiceClient;
import com.perepalacin.order_service.client.ProductServiceClient;
import com.perepalacin.order_service.entity.dao.*;
import com.perepalacin.order_service.entity.dto.*;
import com.perepalacin.order_service.exceptions.NotFoundException;
import com.perepalacin.order_service.exceptions.OperationNotAllowedException;
import com.perepalacin.order_service.exceptions.UnauthorizedException;
import com.perepalacin.order_service.repository.BillingAddressRepository;
import com.perepalacin.order_service.repository.DeliveryAddressRepository;
import com.perepalacin.order_service.repository.PurchaseRepository;
import com.perepalacin.order_service.request.PurchaseRequest;
import com.perepalacin.order_service.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final ProductServiceClient productServiceClient;
    private final CartServiceClient cartServiceClient;
    private final AddressServiceClient addressServiceClient;
    private final PurchaseRepository purchaseRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final ExecutorService executorService;

    public PurchaseDto getById(final long purchaseId) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(
                () -> new NotFoundException("The order with id: " + purchaseId + " doesn't exist")
        );

        if (!purchase.getUserId().equals(userDetails.getUserId())) {
            throw new UnauthorizedException();
        }

        return PurchaseDto.builder()
                .id(purchaseId)
                .status(purchase.getStatus())
                .totalPrice(purchase.getTotalPrice())
                .purchaseItems(purchase.getItems().stream().map(PurchaseItemDto::purchaseItemDaoToDtoMapper).toList())
                .billingAddress(AddressDto.addressDaoToDtoMapper(purchase.getBillingAddress(), true))
                .deliveryAddress(AddressDto.addressDaoToDtoMapper(purchase.getDeliveryAddress(), false))
                .orderDate(purchase.getOrderDate())
                .deliveryDate(purchase.getDeliveryDate())
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .build();
    }

    public List<PurchaseDto> getAllUserOrders () {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        List<Purchase> purchases = purchaseRepository.findByUserId(userDetails.getUserId());
        if (purchases.isEmpty()) {
            return null;
        } 
        
        List<PurchaseDto> purchaseDtos = new ArrayList<>();

        for (Purchase purchase : purchases) {
            purchaseDtos.add(
                PurchaseDto.builder()
                    .id(purchase.getId())
                    .status(purchase.getStatus())
                    .totalPrice(purchase.getTotalPrice())
                    .deliveryAddress(null)
                    .billingAddress(null)
                    .orderDate(purchase.getOrderDate())
                    .deliveryDate(purchase.getDeliveryDate())
                    .build()
            );
        }
        return purchaseDtos;
    }

    @Transactional
    public PurchaseDto createPurchase (PurchaseRequest purchaseRequest) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        CompletableFuture<CartDto> cartFuture = CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<CartDto> response = cartServiceClient.getCartByUserId(userDetails.getUserId());
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    return response.getBody();
                } else {
                    throw new NotFoundException("No cart was found for this user");
                }
            } catch (HttpClientErrorException.NotFound e) {
                    throw new NotFoundException("No cart was found for this user" + e.getMessage());
            } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
                throw new RuntimeException("Error fetching cart for the current user. Server response was: " + e.getMessage(), e);
            }
        }, executorService);

        CompletableFuture<List<AddressDto>> addressesFuture = CompletableFuture.supplyAsync(() -> {
            try {
                ResponseEntity<List<AddressDto>> response = addressServiceClient.getListOfAddresses(purchaseRequest.getBillingAddressId(), purchaseRequest.getDeliveryAddressId());
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    return response.getBody();
                } else {
                    throw new NotFoundException("No valid addresses were found, please try again later");
                }
            } catch (HttpClientErrorException.NotFound e) {
                throw new NotFoundException("No valid addresses were found, please try again later" + e);
            } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
                throw new RuntimeException("Error fetching the user addresses. Server response was: " + e.getMessage(), e);
            }
        }, executorService);

        try {
            return cartFuture.thenCombine(addressesFuture, (cartDto, addressList) -> {
                Optional<AddressDto> billingAddressOptional = addressList.stream()
                        .filter(a -> a.getId().equals(purchaseRequest.getBillingAddressId()))
                        .findFirst();

                Optional<AddressDto> deliveryAddressOptional = addressList.stream()
                        .filter(a -> a.getId().equals(purchaseRequest.getDeliveryAddressId()))
                        .findFirst();

                if (billingAddressOptional.isEmpty()) {
                    throw new NotFoundException("No valid billing address was found with id: " + purchaseRequest.getBillingAddressId() + ", please try again later");
                }
                if (deliveryAddressOptional.isEmpty()) {
                    throw new NotFoundException("No valid delivery address was found with id: " + purchaseRequest.getDeliveryAddressId() + ", please try again later");
                }

                cartDto.getItems().forEach(item -> {
                        if (item.getStock() <= 0) {
                            throw new OperationNotAllowedException("Item " + item.getName() + " has no stock, please remove it from your cart to proceed.");
                        }
                    }
                );

                AddressDto billingAddressDto = billingAddressOptional.get();
                BillingAddress billingAddress = BillingAddress.builder()
                        .fullName(billingAddressDto.getFullName())
                        .addressFirstLine(billingAddressDto.getAddressFirstLine())
                        .addressSecondLine(billingAddressDto.getAddressSecondLine())
                        .telephoneNumber(billingAddressDto.getTelephoneNumber())
                        .postalCode(billingAddressDto.getPostalCode())
                        .city(billingAddressDto.getCity())
                        .province(billingAddressDto.getProvince())
                        .country(billingAddressDto.getCountry())
                        .vatId(billingAddressDto.getVatId())
                        .userId(userDetails.getUserId())
                        .build();

                AddressDto deliveryAddressDto = deliveryAddressOptional.get();
                DeliveryAddress deliveryAddress = DeliveryAddress.builder()
                        .fullName(deliveryAddressDto.getFullName())
                        .addressFirstLine(deliveryAddressDto.getAddressFirstLine())
                        .addressSecondLine(deliveryAddressDto.getAddressSecondLine())
                        .telephoneNumber(deliveryAddressDto.getTelephoneNumber())
                        .postalCode(deliveryAddressDto.getPostalCode())
                        .city(deliveryAddressDto.getCity())
                        .province(deliveryAddressDto.getProvince())
                        .country(deliveryAddressDto.getCountry())
                        .userId(userDetails.getUserId())
                        .build();

                List<PurchaseItem> purchaseItems = cartDto.getItems().stream().map(PurchaseItem::cartItemDtoToPurchaseItemMapper).toList();

                Purchase purchase = new Purchase().builder()
                        .status("PAID")
                        .totalPrice(cartDto.getTotalPrice())
                        .billingAddress(billingAddress)
                        .deliveryAddress(deliveryAddress)
                        .deliveryDate(LocalDateTime.now().plusDays(2))
                        .items(purchaseItems)
                        .build();

                cartServiceClient.deleteUserCart();
                purchaseRepository.save(purchase);


                 return PurchaseDto.purchaseDaoToDtoMapper(purchase, this.mapCartItemsToPurchaseItems(purchase.getItems(), cartDto.getItems()));

            }).join();

        } catch (CompletionException e) {
            throw new RuntimeException("An unexpected error occurred during purchase creation.", e);
        }
    }

    public PurchaseDto editPurchaseAddress(final Long purchaseId, final Long addressId, final boolean isBillingAddress) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        Purchase purchaseToEdit = purchaseRepository.findById(purchaseId).orElseThrow(
                () -> new NotFoundException("Purchase with id: " + purchaseId + " could not be found")
        );

        if (!purchaseToEdit.getUserId().equals(userDetails.getUserId())) {
            throw new UnauthorizedException();
        }

        if (!isBillingAddress && LocalDateTime.now().isAfter(purchaseToEdit.getDeliveryDate())){
            throw new OperationNotAllowedException("The purchase has already been delivered, the delivery date can't be modified.");
        }

        AddressDto fetchedAddress;
        try {
            ResponseEntity<AddressDto> addressResponse = addressServiceClient.getAddressById(addressId);
            if (addressResponse.getStatusCode().is2xxSuccessful() && addressResponse.getBody() != null) {
                fetchedAddress = addressResponse.getBody();
            } else {
                throw new NotFoundException("No valid address was found, please try again later");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("No valid address was found, please try again later" + e);
        } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
            throw new RuntimeException("Error fetching the user address. Server response was: " + e.getMessage(), e);
        }

        if (isBillingAddress) {
            BillingAddress newBillingAddress = BillingAddress.builder()
                    .id(fetchedAddress.getId())
                    .fullName(fetchedAddress.getFullName())
                    .telephoneNumber(fetchedAddress.getTelephoneNumber())
                    .addressFirstLine(fetchedAddress.getAddressFirstLine())
                    .addressSecondLine(fetchedAddress.getAddressSecondLine())
                    .postalCode(fetchedAddress.getPostalCode())
                    .city(fetchedAddress.getCity())
                    .province(fetchedAddress.getProvince())
                    .country(fetchedAddress.getCountry())
                    .vatId(fetchedAddress.getVatId())
                    .build();
            purchaseToEdit.setBillingAddress(newBillingAddress);
        } else {
            DeliveryAddress newDeliveryAddress = DeliveryAddress.builder()
                    .id(fetchedAddress.getId())
                    .fullName(fetchedAddress.getFullName())
                    .telephoneNumber(fetchedAddress.getTelephoneNumber())
                    .addressFirstLine(fetchedAddress.getAddressFirstLine())
                    .addressSecondLine(fetchedAddress.getAddressSecondLine())
                    .postalCode(fetchedAddress.getPostalCode())
                    .city(fetchedAddress.getCity())
                    .province(fetchedAddress.getProvince())
                    .country(fetchedAddress.getCountry())
                    .build();
            purchaseToEdit.setDeliveryAddress(newDeliveryAddress);
        }

        purchaseRepository.save(purchaseToEdit);

        return PurchaseDto.purchaseDaoToDtoMapper(purchaseToEdit, purchaseToEdit.getItems().stream().map(PurchaseItemDto::purchaseItemDaoToDtoMapper).toList());
    }

    @Transactional
    public PurchaseDto editPurchaseItemQuantity (final Long purchaseId, final Long itemId, final Integer newQuantity) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        Purchase purchaseToEdit = purchaseRepository.findById(purchaseId).orElseThrow(
                () -> new NotFoundException("Purchase with id: " + purchaseId + " could not be found")
        );

        if (!purchaseToEdit.getUserId().equals(userDetails.getUserId())) {
            throw new UnauthorizedException();
        }

        if (LocalDateTime.now().isAfter(purchaseToEdit.getDeliveryDate())){
            throw new OperationNotAllowedException("The purchase has already been delivered, you can no longer edit it.");
        }

        for (int i = 0; i < purchaseToEdit.getItems().size(); i++) {
            if (purchaseToEdit.getItems().get(i).getId().equals(itemId)) {
                if (purchaseToEdit.getItems().get(i).getQuantity() > newQuantity) {
                    int oldQuantity = purchaseToEdit.getItems().get(i).getQuantity();
                    purchaseToEdit.getItems().get(i).setQuantity(newQuantity);
                    BigDecimal newPrice = purchaseToEdit.getTotalPrice().subtract(purchaseToEdit.getItems().get(i).getPurchase_price().multiply(new BigDecimal(oldQuantity))).add(purchaseToEdit.getItems().get(i).getPurchase_price().multiply(new BigDecimal(newQuantity)));
                    purchaseToEdit.setTotalPrice(newPrice);
                    purchaseRepository.save(purchaseToEdit);
                    break;
                } else if (purchaseToEdit.getItems().get(i).getQuantity() < newQuantity) {
                    int oldQuantity = purchaseToEdit.getItems().get(i).getQuantity();
                    ProductDto productDto;
                    try {
                        ResponseEntity<ProductDto> productResponse = productServiceClient.getProductById(purchaseToEdit.getItems().get(i).getProductId());
                        if (productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
                            productDto = productResponse.getBody();
                        } else {
                            throw new NotFoundException("The product you want to modify no longer exists, please contact us directly to fix it.");
                        }
                    } catch (HttpClientErrorException.NotFound e) {
                        throw new NotFoundException("The product you want to modify no longer exists, please contact us directly to fix it." + e);
                    } catch (HttpClientErrorException | HttpServerErrorException | ResourceAccessException e) {
                        throw new RuntimeException("Error fetching the product to edit. Server response was: " + e.getMessage(), e);
                    }
                    if (productDto.getPrice().equals(purchaseToEdit.getItems().get(i).getPurchase_price())) {
                        purchaseToEdit.getItems().get(i).setQuantity(newQuantity);
                        BigDecimal newPrice = purchaseToEdit.getTotalPrice().subtract(purchaseToEdit.getItems().get(i).getPurchase_price().multiply(new BigDecimal(oldQuantity))).add(purchaseToEdit.getItems().get(i).getPurchase_price().multiply(new BigDecimal(newQuantity)));
                        purchaseToEdit.setTotalPrice(newPrice);
                    } else {
                        purchaseToEdit.getItems().get(i).setQuantity(newQuantity);
                        purchaseToEdit.getItems().get(i).setPurchase_price(
                                purchaseToEdit.getItems().get(i).getPurchase_price().multiply(new BigDecimal(purchaseToEdit.getItems().get(i).getQuantity()))
                                        .add(productDto.getPrice().multiply(new BigDecimal(newQuantity - oldQuantity)))
                                        .divide(new BigDecimal(newQuantity), RoundingMode.HALF_EVEN));
                        BigDecimal newPrice = purchaseToEdit.getTotalPrice().add(productDto.getPrice().multiply(new BigDecimal(newQuantity-oldQuantity)));
                        purchaseToEdit.setTotalPrice(newPrice);
                    }
                    purchaseRepository.save(purchaseToEdit);
                    break;
                }
            }
        }
        return PurchaseDto.purchaseDaoToDtoMapper(purchaseToEdit, purchaseToEdit.getItems().stream().map(PurchaseItemDto::purchaseItemDaoToDtoMapper).toList());
    }

    public PurchaseDto removePurchaseItem (final Long purchaseId, final Long itemId) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        Purchase purchaseToEdit = purchaseRepository.findById(purchaseId).orElseThrow(
                () -> new NotFoundException("Purchase with id: " + purchaseId + " could not be found")
        );

        if (!purchaseToEdit.getUserId().equals(userDetails.getUserId())) {
            throw new UnauthorizedException();
        }

        if (LocalDateTime.now().isAfter(purchaseToEdit.getDeliveryDate())) {
            throw new OperationNotAllowedException("The purchase has already been delivered, you can no longer edit it.");
        }

        List<PurchaseItem> newPurchaseItems = new ArrayList<>();
        for (int i = 0; i < purchaseToEdit.getItems().size(); i++) {
            if (purchaseToEdit.getItems().get(i).getId().equals(itemId)) {
                purchaseToEdit.setTotalPrice(purchaseToEdit.getTotalPrice().subtract(purchaseToEdit.getItems().get(i).getPurchase_price().multiply(new BigDecimal(purchaseToEdit.getItems().get(i).getQuantity()))));
            } else {
                newPurchaseItems.add(purchaseToEdit.getItems().get(i));
            }
        }
        purchaseToEdit.setItems(newPurchaseItems);
        purchaseRepository.save(purchaseToEdit);
        return PurchaseDto.purchaseDaoToDtoMapper(purchaseToEdit, purchaseToEdit.getItems().stream().map(PurchaseItemDto::purchaseItemDaoToDtoMapper).toList());
    }

    public void cancelPurchase (final long purchaseId) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        Purchase purchaseToCancel = purchaseRepository.findById(purchaseId).orElseThrow(
                () -> new NotFoundException("Purchase with id: " + purchaseId + " not found")
        );

        if (!purchaseToCancel.getUserId().equals(userDetails.getUserId())) {
            throw new UnauthorizedException();
        }

        if (LocalDateTime.now().isAfter(purchaseToCancel.getDeliveryDate())){
            throw new OperationNotAllowedException("The purchase has already been delivered, please ask for a refund instead.");
        }

        purchaseRepository.delete(purchaseToCancel);
    }

    private List<PurchaseItemDto> mapCartItemsToPurchaseItems (List<PurchaseItem> purchaseItems, List<CartItemDto> cartItems) {
        List<PurchaseItemDto> result = new ArrayList<>();
        for (PurchaseItem purchaseItem : purchaseItems) {
            for (CartItemDto item : cartItems) {
                if (item.getId().equals(purchaseItem.getProductId())) {
                    result.add(PurchaseItemDto.purchaseItemDaoToDtoMapper(purchaseItem, item));
                }
            }
        }
        return result;
    }
}
