package com.perepalacin.order_service.entity.dao;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="purchases_delivery_addresses")
public class DeliveryAddress implements AddressDetails {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private UUID userId;
    @Column(name="full_name")
    private String fullName;
    @Column(name="telephone_number")
    private String telephoneNumber;
    @Column(name="address_first_line")
    private String addressFirstLine;
    @Column(name="address_second_line")
    private String addressSecondLine;
    @Column(name="postal_code")
    private String postalCode;
    @Column(name="city")
    private String city;
    @Column(name="province")
    private String province;
    @Column(name="country")
    private String country;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;
}
