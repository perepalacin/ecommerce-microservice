package com.perepalacin.order_service.entity.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String publicUrl;
    private String brand;
    private String description;
    private String imageUrl;
    private String mechanism; // e.g., "Automatic", "Quartz", "Manual"
    private Integer diameter; // in mm
    private BigDecimal price;
    private Integer stock;
}
