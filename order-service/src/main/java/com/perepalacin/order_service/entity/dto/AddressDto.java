package com.perepalacin.order_service.entity.dto;

import com.perepalacin.order_service.entity.dao.AddressDetails;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public static AddressDto addressDaoToDtoMapper (final AddressDetails addressDetails, final boolean isBillingAddress) {
        return AddressDto.builder()
                .id(addressDetails.getId())
                .fullName(addressDetails.getFullName())
                .telephoneNumber(addressDetails.getTelephoneNumber())
                .addressFirstLine(addressDetails.getAddressFirstLine())
                .addressSecondLine(addressDetails.getAddressSecondLine())
                .postalCode(addressDetails.getPostalCode())
                .city(addressDetails.getCity())
                .province(addressDetails.getProvince())
                .country(addressDetails.getCountry())
                .vatId(isBillingAddress ? addressDetails.getVatId() : null)
                .build();
    }
}
