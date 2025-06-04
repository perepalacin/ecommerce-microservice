package com.perepalacin.notification_service.entity.dto;

import lombok.*;

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
}
