package com.perepalacin.order_service.entity.dto;

import com.perepalacin.order_service.entity.dao.Purchase;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDto {
    private Long id;
    private String status;
    private BigDecimal totalPrice;
    private List<PurchaseItemDto> purchaseItems;
    private LocalDateTime deliveryDate;
    private LocalDateTime orderDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AddressDto deliveryAddress;
    private AddressDto billingAddress;

    public static PurchaseDto purchaseDaoToDtoMapper (Purchase purchaseDao, List<PurchaseItemDto> purchaseItems) {
        return PurchaseDto.builder()
                .id(purchaseDao.getId())
                .status(purchaseDao.getStatus())
                .totalPrice(purchaseDao.getTotalPrice())
                .purchaseItems(purchaseDao.getItems().stream().map(PurchaseItemDto::purchaseItemDaoToDtoMapper).toList())
                .deliveryDate(purchaseDao.getDeliveryDate())
                .orderDate(purchaseDao.getOrderDate())
                .createdAt(purchaseDao.getCreatedAt())
                .updatedAt(purchaseDao.getUpdatedAt())
                .purchaseItems(purchaseItems)
                .deliveryAddress(AddressDto.addressDaoToDtoMapper(purchaseDao.getDeliveryAddress(), false))
                .billingAddress(AddressDto.addressDaoToDtoMapper(purchaseDao.getBillingAddress(), true))
                .build();
    }
}
