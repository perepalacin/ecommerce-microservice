package com.perepalacin.cart_service.entity.dao;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "restock_alerts")
public class RestockAlert {

    @Id
    @Column(name="id")
    private Long id;

    @Column(name="product_id")
    private Long productId;

    @Column(name="user_email")
    private String userEmail;

}

