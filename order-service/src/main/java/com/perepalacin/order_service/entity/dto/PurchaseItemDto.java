package com.perepalacin.order_service.entity.dto;

import com.perepalacin.order_service.entity.dao.PurchaseItem;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemDto {
    private Long id;
    private Integer quantity;
    private BigDecimal purchase_price;
    private Long productId;
    private String name;
    private String publicUrl;
    private String imageUrl;

    public static PurchaseItemDto purchaseItemDaoToDtoMapper (PurchaseItem purchaseItem, CartItemDto cartItemDto) {
        return PurchaseItemDto.builder()
                .id(purchaseItem.getId())
                .productId(purchaseItem.getProductId())
                .name(cartItemDto != null ? cartItemDto.getName() : null)
                .publicUrl(cartItemDto != null ? cartItemDto.getPublicUrl() : null)
                .imageUrl(cartItemDto != null ? cartItemDto.getImageUrl() : null)
                .quantity(purchaseItem.getQuantity())
                .purchase_price(purchaseItem.getPurchase_price())
                .build();
    }

    public static PurchaseItemDto purchaseItemDaoToDtoMapper (PurchaseItem purchaseItem) {
        return purchaseItemDaoToDtoMapper(purchaseItem, null);
    }
}
