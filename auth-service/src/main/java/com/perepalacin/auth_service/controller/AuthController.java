package com.perepalacin.auth_service.controller;

import com.perepalacin.auth_service.client.OAuthClient;
import com.perepalacin.auth_service.response.KeycloakTokenResponse;
import com.perepalacin.auth_service.entity.dto.UserDto;
import com.perepalacin.auth_service.service.KeycloakService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

import static com.perepalacin.auth_service.util.CookiesUtil.addHttpOnlyCookie;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakService keycloakService;
    private final OAuthClient oAuthClient;
    final int defaultRefreshMaxAge = 60 * 60 * 24 * 14; // 14 days
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong!");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserDto userDto, HttpServletResponse response) {
        try {
            KeycloakTokenResponse token = oAuthClient.authenticateUser(userDto);
            String accessToken = token.getAccessToken();
            String refreshToken = token.getRefreshToken();
            Integer expiresIn = token.getExpiresIn();
            Integer refreshExpiresIn = token.getRefreshExpiresIn();

            addHttpOnlyCookie(response, "authToken", accessToken, expiresIn, false);
            addHttpOnlyCookie(response, "refreshToken", refreshToken, refreshExpiresIn == null ? defaultRefreshMaxAge : refreshExpiresIn, false);

            return ResponseEntity.ok().body("Login successful");

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            return ResponseEntity.status(500).body("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token/{refreshToken}")
    public ResponseEntity<?> getNewAccessToken(@PathVariable(name="refreshToken") String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        String existingRefreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    existingRefreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (existingRefreshToken == null) {
            return ResponseEntity.status(401).body("Refresh token not found.");
        }

        try {
            KeycloakTokenResponse token = oAuthClient.refreshAccessToken(refreshToken);
            String newAccessToken = token.getAccessToken();
            String newRefreshToken = token.getRefreshToken();
            Integer newExpiresIn = token.getExpiresIn();
            Integer newRefreshExpiresIn = token.getRefreshExpiresIn();


            addHttpOnlyCookie(response, "authToken", newAccessToken, newExpiresIn, false);
            addHttpOnlyCookie(response, "refreshToken", newRefreshToken, newRefreshExpiresIn == null ? defaultRefreshMaxAge : newRefreshExpiresIn, false);

            return ResponseEntity.ok().body("Token refreshed successfully.");

        } catch (Exception e) {
            System.err.println("Refresh token error: " + e.getMessage());
            return ResponseEntity.status(500).body("Token refresh failed: " + e.getMessage());
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDTO) throws URISyntaxException {
        ResponseEntity<String> response = keycloakService.createUser(userDTO);
        return response;
    }


    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable String userId, @RequestBody UserDto userDTO){
        return keycloakService.updateUser(userId, userDTO);
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        return keycloakService.deleteUser(userId);
    }

}