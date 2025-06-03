package com.perepalacin.order_service.client;

import com.perepalacin.order_service.entity.dto.AddressDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AddressServiceClient {

    @Value("${address.service.url}")
    private String addressServiceUrl;

    public ResponseEntity<List<AddressDto>> getListOfAddresses(final Long billingAddressId, final Long deliveryAddressId) {
        RestTemplate restTemplate = new RestTemplate();

        ParameterizedTypeReference<List<AddressDto>> responseType =
            new ParameterizedTypeReference<List<AddressDto>>() {};
	    HttpEntity<List<Long>> requestEntity;

        if (billingAddressId.equals(deliveryAddressId)) {
            requestEntity = new HttpEntity<>(List.of(billingAddressId));
        } else {
            requestEntity = new HttpEntity<>(List.of(billingAddressId, deliveryAddressId));
        }

        return restTemplate.exchange(
                addressServiceUrl + "batch",
                HttpMethod.POST,
                requestEntity,
                responseType
        );
    }
}
