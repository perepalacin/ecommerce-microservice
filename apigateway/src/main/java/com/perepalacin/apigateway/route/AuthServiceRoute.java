package com.perepalacin.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class AuthServiceRoute {

    @Bean
    public RouterFunction<ServerResponse> authServiceRoutes() {
        return GatewayRouterFunctions.route("auth-service")
                .route(RequestPredicates.path("/api/v1/auth/**"),
                        HandlerFunctions.http("http://localhost:8082"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("authServiceCircuitBreaker",
                        URI.create("forward:/authFallback")))
                .build();
    }
}
