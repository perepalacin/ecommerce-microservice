package com.perepalacin.product_service.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestockEvent {
    private List<String> emailList;
    private Product product;
}
