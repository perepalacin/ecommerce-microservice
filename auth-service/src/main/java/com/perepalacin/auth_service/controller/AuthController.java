package com.perepalacin.auth_service.controller;

import com.perepalacin.auth_service.entity.dto.UserDto;
import com.perepalacin.auth_service.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private KeycloakService keycloakService;


    @GetMapping("/search")
    public ResponseEntity<?> findAllUsers(){
        return ResponseEntity.ok(keycloakService.findAllUsers());
    }


    @GetMapping("/search/{username}")
    public ResponseEntity<?> searchUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
    }


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDTO) throws URISyntaxException {
        String response = keycloakService.createUser(userDTO);
        return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
    }


    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDto userDTO){
        keycloakService.updateUser(userId, userDTO);
        return ResponseEntity.ok("User updated successfully");
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId){
        keycloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ping")
    public String pingAuthService() {
        return "Pong from the auth service";
    }
}