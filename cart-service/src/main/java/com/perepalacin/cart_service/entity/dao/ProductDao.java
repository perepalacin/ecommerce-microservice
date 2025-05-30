package com.perepalacin.cart_service.entity.dao;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDao {
    private Long id;
    private String name;
    private String brand;
    private String mechanism; // e.g., "Automatic", "Quartz", "Manual"
    private Integer diameter; // in mm
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String publicUrl;
    private String imageUrl;
}
