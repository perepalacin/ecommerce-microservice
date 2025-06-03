package com.perepalacin.order_service.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequest {
    private Long cartId;
    private Long billingAddressId;
    private Long deliveryAddressId;
}
