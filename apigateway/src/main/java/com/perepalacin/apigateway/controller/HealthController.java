package com.perepalacin.apigateway.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/ping-admin")
    @PreAuthorize("hasRole('admin')")
    public String pingAdmin() {
        return "pong! - Admin";
    }

    @GetMapping("/ping-user")
    @PreAuthorize("hasRole('user')")
    public String pingUser() {
        return "pong!";
    }

    @GetMapping("/ping")
    public String ping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt principal = (Jwt) authentication.getPrincipal();

            String userId = principal.getSubject(); // 'sub' claim
            String email = principal.getClaimAsString("email");
            String preferredUsername = principal.getClaimAsString("preferred_username");

            System.out.println("Current User ID: " + userId);
            System.out.println("Current User Email: " + email);
            System.out.println("Current User Preferred Username: " + preferredUsername);

        } else {
            System.out.println("User not authenticated with a JWT or principal is not a Jwt object.");
        }
        return "pong!";
    }
}
