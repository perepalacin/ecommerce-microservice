package com.perepalacin.cart_service.util;

import com.perepalacin.cart_service.entity.dto.UserDetailsDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class JwtUtil {

    public static com.perepalacin.cart_service.entity.dto.UserDetailsDto getCredentialsFromToken () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = null;
        String email = null;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt jwt) {
                userId = jwt.getSubject();
                email = jwt.getClaimAsString("email");

                return UserDetailsDto.builder()
                        .email(email)
                        .userId(UUID.fromString(userId))
                        .build();
            }
        }
        return null;
    }

}
