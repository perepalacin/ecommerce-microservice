package com.perepalacin.order_service.entity.dao;

import java.util.UUID;

public interface AddressDetails {

    Long getId();
    UUID getUserId();
    String getFullName();
    String getTelephoneNumber();
    String getAddressFirstLine();
    String getAddressSecondLine();
    String getPostalCode();
    String getCity();
    String getProvince();
    String getCountry();
    String getVatId();

    void setId(Long id);
    void setUserId(UUID userId);
    void setFullName(String fullName);
    void setTelephoneNumber(String telephoneNumber);
    void setAddressFirstLine(String addressFirstLine);
    void setAddressSecondLine(String addressSecondLine);
    void setPostalCode(String postalCode);
    void setCity(String city);
    void setProvince(String province);
    void setCountry(String country);
    void setVatId(String vatId);
}
