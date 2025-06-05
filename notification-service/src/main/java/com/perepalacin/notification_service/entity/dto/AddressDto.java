package com.perepalacin.notification_service.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private String fullName;
    private String telephoneNumber;
    private String addressFirstLine;
    private String addressSecondLine;
    private String postalCode;
    private String city;
    private String province;
    private String country;
    private String vatId;

}
