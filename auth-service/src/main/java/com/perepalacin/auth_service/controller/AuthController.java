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

import java.net.URI;
import java.net.URISyntaxException;

import static com.perepalacin.auth_service.util.CookiesUtil.addHttpOnlyCookie;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakService keycloakService;
    private final OAuthClient oAuthClient;
    final int defaultRefreshMaxAge = Integer.MAX_VALUE;
//    final int defaultRefreshMaxAge = 60 * 60 * 24 * 14; // 14 days
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong!");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserDto userDto, HttpServletResponse response) {
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

    @PostMapping("/refresh-token")
    public ResponseEntity<String> getNewAccessToken(HttpServletRequest request, HttpServletResponse response) {
        //TODO: Should remove cookies and redirect to sign-in if it fails!
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
            KeycloakTokenResponse token = oAuthClient.refreshAccessToken(existingRefreshToken);
            String newAccessToken = token.getAccessToken();
            String newRefreshToken = token.getRefreshToken();
            Integer newExpiresIn = token.getExpiresIn();
            Integer newRefreshExpiresIn = token.getRefreshExpiresIn();


            addHttpOnlyCookie(response, "authToken", newAccessToken, newExpiresIn, false);
            addHttpOnlyCookie(response, "refreshToken", newRefreshToken, newRefreshExpiresIn == null ? defaultRefreshMaxAge : newRefreshExpiresIn, false);

            return ResponseEntity.ok().body("Token refreshed successfully.");

        } catch (Exception e) {
            System.err.println("Refresh token error: " + e.getMessage());
            Cookie authTokenCookie = new Cookie("authToken", null);
            authTokenCookie.setHttpOnly(true);
            authTokenCookie.setPath("/");
            authTokenCookie.setMaxAge(0);
            response.addCookie(authTokenCookie);
            Cookie refreshTokenCookie = new Cookie("refreshToken", null);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(0);
            response.addCookie(refreshTokenCookie);
            try {
                return ResponseEntity.status(HttpStatus.SEE_OTHER)
                        .location(new URI("/auth/login"))
                        .build();
            } catch (URISyntaxException uriEx) {
                System.err.println("Error creating redirect URI for /auth/login: " + uriEx.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Token refresh failed and could not redirect: " + e.getMessage());
            }
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDTO) throws URISyntaxException {
        ResponseEntity<String> response = keycloakService.createUser(userDTO);
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response) {
        Cookie authTokenCookie = new Cookie("authToken", null);
        authTokenCookie.setHttpOnly(true);
        authTokenCookie.setPath("/");
        authTokenCookie.setMaxAge(0);
        response.addCookie(authTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
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