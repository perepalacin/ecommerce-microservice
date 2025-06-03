package com.perepalacin.order_service.entity.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private List<CartItemDto> items = new ArrayList<>();

    public BigDecimal getTotalPrice () {
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItemDto cartItemDto : this.items) {
            totalPrice.add(cartItemDto.getPrice().multiply(new BigDecimal(cartItemDto.getQuantity())));
        }
        return totalPrice;
    }
}
