package com.perepalacin.auth_service.controller;

import com.perepalacin.auth_service.client.OAuthClient;
import com.perepalacin.auth_service.response.KeycloakTokenResponse;
import com.perepalacin.auth_service.entity.dto.UserDto;
import com.perepalacin.auth_service.service.KeycloakService;
import com.perepalacin.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakService keycloakService;
    private final OAuthClient oAuthClient;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong!");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<KeycloakTokenResponse> loginUser(@Valid @RequestBody UserDto userDto) {
        KeycloakTokenResponse token = oAuthClient.authenticateUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/refresh-token/{refreshToken}")
    public ResponseEntity<KeycloakTokenResponse> getNewAccessToken(@PathVariable(name="refreshToken") String refreshToken) {
        KeycloakTokenResponse token = oAuthClient.refreshAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(token);
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