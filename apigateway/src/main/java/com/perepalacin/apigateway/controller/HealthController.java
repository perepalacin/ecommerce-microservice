package com.perepalacin.apigateway.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/ping-admin")
    @PreAuthorize("hasRole('admin')")
    public String pingAdmin() {
        return "pong! - Admin";
    }

    @GetMapping("/ping")
    @PreAuthorize("hasRole('user')")
    public String ping() {
        return "pong!";
    }
}
