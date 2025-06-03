package com.perepalacin.auth_service.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("You are not authorized to perform this operation");
    }
}
