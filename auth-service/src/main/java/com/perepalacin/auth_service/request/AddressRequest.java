package com.perepalacin.auth_service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    @NotNull(message = "Full name is required")
    private String fullName;
    @NotNull(message = "Telephone number is required")
    private short telephoneNumber;
    @NotNull(message = "Address first line is required")
    private String addressFirstLine;
    @NotNull(message = "Address second line is required")
    private String addressSecondLine;
    @NotNull(message = "Postal code is required")
    private String postalCode;
    @NotNull(message = "City is required")
    private String city;
    @NotNull(message = "Province is required")
    private String province;
    @NotNull(message = "Country is required")
    private String country;
    @NotNull(message = "VAT ID is required")
    private String vatId;
    private boolean defaultAddress;
}
