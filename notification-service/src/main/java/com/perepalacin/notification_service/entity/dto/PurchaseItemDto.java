package com.perepalacin.notification_service.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseItemDto {
    private Long id;
    private Integer quantity;
    private BigDecimal purchase_price;
    private Long productId;
    private String name;
    private String publicUrl;
    private String imageUrl;
}
