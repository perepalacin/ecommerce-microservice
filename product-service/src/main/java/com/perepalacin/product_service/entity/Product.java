package com.perepalacin.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name="products")
public class Product {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="brand")
    private String brand;
    @Column(name="mechanism")
    private String mechanism; // e.g., "Automatic", "Quartz", "Manual"
    @Column(name="diameter")
    private Integer diameter; // in mm
    @Column(name="price")
    private BigDecimal price;
    @Column(name="stock")
    private Integer stock;
    @Column(name="description")
    private String description;
    @Column(name="public_url")
    private String publicUrl;
    @Column(name="imageUrl")
    private String imageUrl;
}
