package com.perepalacin.auth_service.entity.dao;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="addresses")
public class AddressDao {
    @Id
    @Column(name="id")
    private long id;
    @Column(name="full_name")
    private String fullName;
    @Column(name="telephone_number")
    private String telephoneNumber;
    @Column(name="address_first_line")
    private String addressFirstLine;
    @Column(name="address_second_line")
    private String addressSecondLine;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name="city")
    private String city;
    @Column(name="province")
    private String province;
    @Column(name="country")
    private String country;
    @Column(name="vat_id")
    private String vatId;
    @Column(name="default_address")
    private boolean defaultAddress;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserDao userId;
}
