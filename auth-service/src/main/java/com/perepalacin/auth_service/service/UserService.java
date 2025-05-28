package com.perepalacin.auth_service.service;

import com.perepalacin.auth_service.entity.dao.UserDao;
import com.perepalacin.auth_service.entity.dto.UserDetailsDto;
import com.perepalacin.auth_service.entity.dto.UserDto;
import com.perepalacin.auth_service.repository.UserRepository;
import com.perepalacin.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserDao userDao){
        userRepository.saveAndFlush(userDao);
    }

    public boolean updateUser(UUID userId, UserDto userDto) {

        UserDetailsDto userDetailsDto = JwtUtil.getCredentialsFromToken();
        if (userDetailsDto == null) {
            return false;
        }

        final UserDao user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Couldn't find the user in question")
        );

        if (!user.getId().equals(userDetailsDto.getUserId())) {
            return false;
        }

        userRepository.saveAndFlush(UserDao.builder()
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build());
        return true;
    }

    public boolean deleteUser(UUID userId) {

        UserDetailsDto userDetailsDto = JwtUtil.getCredentialsFromToken();
        if (userDetailsDto == null) {
            return false;
        }

        final UserDao user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Couldn't find the user in question")
        );

        if (!user.getId().equals(userDetailsDto.getUserId())) {
            return false;
        }

        userRepository.delete(UserDao.builder()
                .id(userId)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build());
        return true;
    }
}
