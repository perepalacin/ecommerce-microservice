package com.perepalacin.cart_service.entity.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.perepalacin.cart_service.entity.dao.CartItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private List<CartItemDto> items = new ArrayList<>();
}
