package com.perepalacin.order_service.entity.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.perepalacin.order_service.entity.dto.CartItemDto;
import com.perepalacin.order_service.entity.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="purchases_item")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal purchase_price;

    public static PurchaseItem cartItemDtoToPurchaseItemMapper (CartItemDto cartItemDto) {
        return PurchaseItem.builder()
                .productId(cartItemDto.getProductId())
                .quantity(cartItemDto.getQuantity())
                .purchase_price(cartItemDto.getPrice())
                .build();
    }

}
