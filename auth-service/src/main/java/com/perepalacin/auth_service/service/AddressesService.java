package com.perepalacin.auth_service.service;

import com.perepalacin.auth_service.entity.dao.AddressDao;
import com.perepalacin.auth_service.entity.dao.UserDao;
import com.perepalacin.auth_service.entity.dto.UserDetailsDto;
import com.perepalacin.auth_service.exceptions.NotFoundException;
import com.perepalacin.auth_service.repository.AddressesRepository;
import com.perepalacin.auth_service.request.AddressRequest;
import com.perepalacin.auth_service.util.JwtUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressesService {

    private final AddressesRepository addressesRepository;

    public List<AddressDao> getUserAddresses () {
        UserDetailsDto userDetailsDto = JwtUtil.getCredentialsFromToken();
        if (userDetailsDto == null) {
            return null;
        }
        return addressesRepository.findByUserId(userDetailsDto.getUserId());
    }

    @Transactional
    public AddressDao addAddress (AddressRequest addressRequest) {
        UserDetailsDto userDetailsDto = JwtUtil.getCredentialsFromToken();
        if (userDetailsDto == null) {
            return null;
        }
        AddressDao newAddress = AddressDao.builder()
                .fullName(addressRequest.getFullName())
                .telephoneNumber(addressRequest.getTelephoneNumber())
                .addressFirstLine(addressRequest.getAddressFirstLine())
                .addressSecondLine(addressRequest.getAddressSecondLine())
                .postalCode(addressRequest.getPostalCode())
                .city(addressRequest.getCity())
                .province(addressRequest.getProvince())
                .country(addressRequest.getCountry())
                .vatId(addressRequest.getVatId())
                .defaultAddress(addressRequest.isDefaultAddress())
                .userId(UserDao.builder().id(userDetailsDto.getUserId()).build())
                .build();

        if (addressRequest.isDefaultAddress()) {
            modifyCurrentDefaultAddress(userDetailsDto.getUserId());
        }
        addressesRepository.save(newAddress);
        return newAddress;
    }

    @Transactional
    public AddressDao editAddress (long addressId, AddressRequest addressRequest) {
        UserDetailsDto userDetailsDto = JwtUtil.getCredentialsFromToken();
        if (userDetailsDto == null) {
            return null;
        }
        AddressDao addressToEdit = addressesRepository.findByIdAndUserId(addressId, userDetailsDto.getUserId()).orElseThrow(
                () -> new NotFoundException("Address with id: " + addressId + " does not exist")
        );
        addressToEdit.setFullName(addressRequest.getFullName());
        addressToEdit.setTelephoneNumber(addressRequest.getTelephoneNumber());
        addressToEdit.setAddressFirstLine(addressRequest.getAddressFirstLine());
        addressToEdit.setAddressSecondLine(addressRequest.getAddressSecondLine());
        addressToEdit.setPostalCode(addressRequest.getPostalCode());
        addressToEdit.setCity(addressToEdit.getCity());
        addressToEdit.setProvince(addressRequest.getProvince());
        addressToEdit.setCountry(addressToEdit.getCountry());
        addressToEdit.setVatId(addressToEdit.getVatId());
        addressToEdit.setDefaultAddress(addressToEdit.isDefaultAddress());

        if (addressRequest.isDefaultAddress()) {
            modifyCurrentDefaultAddress(userDetailsDto.getUserId(), addressId);
        }

        addressesRepository.save(addressToEdit);
        return addressToEdit;
    }

    @Transactional
    public void editDefaultAddress (long addressId) {
        UserDetailsDto userDetailsDto = JwtUtil.getCredentialsFromToken();
        if (userDetailsDto == null) {
            return;
        }
        AddressDao addressToEdit = addressesRepository.findByIdAndUserId(addressId, userDetailsDto.getUserId()).orElseThrow(
                () -> new NotFoundException("Address with id: " + addressId + " does not exist")
        );
        modifyCurrentDefaultAddress(userDetailsDto.getUserId(), addressId);
        addressToEdit.setDefaultAddress(true);
    }

    public void deleteAddress (long addressId) {
        UserDetailsDto userDetailsDto = JwtUtil.getCredentialsFromToken();
        if (userDetailsDto == null) {
            return;
        }
        AddressDao addressToDelete = addressesRepository.findByIdAndUserId(addressId, userDetailsDto.getUserId()).orElseThrow(
                () -> new NotFoundException("Address with id: " + addressId + " does not exist")
        );

        addressesRepository.delete(addressToDelete);
    }

    private void modifyCurrentDefaultAddress (UUID userId) {
        Optional<AddressDao> currentDefaultAddressOptional = addressesRepository.findByUserIdAndDefaultAddressTrue(userId);
        if (currentDefaultAddressOptional.isPresent()) {
            AddressDao currentDefaultAddress = currentDefaultAddressOptional.get();
            currentDefaultAddress.setDefaultAddress(false);
            addressesRepository.save(currentDefaultAddress);
        }
    }

    private void modifyCurrentDefaultAddress (UUID userId, long addressId) {
        Optional<AddressDao> currentDefaultAddressOptional = addressesRepository.findByUserIdAndDefaultAddressTrue(userId);
        if (currentDefaultAddressOptional.isPresent()) {
            AddressDao currentDefaultAddress = currentDefaultAddressOptional.get();
            if (currentDefaultAddress.getId() == addressId) {
                return;
            }
            currentDefaultAddress.setDefaultAddress(false);
            addressesRepository.save(currentDefaultAddress);
        }
    }

}
