package com.perepalacin.notification_service.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseEmailData {
    private String eventType;
    private String recipientFullName;
    private String email;
    private BigDecimal totalPrice;
    private Long purchaseId;
    private AddressDto deliveryAddress;
    private LocalDateTime deliveryDate;
    private List<PurchaseItemDto> purchaseItems;
}
