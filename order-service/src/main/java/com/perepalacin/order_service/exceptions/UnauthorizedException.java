package com.perepalacin.order_service.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("You are not properly authenticated, please log back into the platform.");
    }
}
