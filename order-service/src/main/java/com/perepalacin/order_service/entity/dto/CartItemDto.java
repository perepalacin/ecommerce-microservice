package com.perepalacin.order_service.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CartItemDto {
    private Long id;
    private Long productId;
    private String name;
    private String publicUrl;
    private String imageUrl;
    private BigDecimal price;
    private int quantity;
    private int stock;
}
