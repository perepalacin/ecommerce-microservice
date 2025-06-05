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
public class PurchaseServiceRoutes {
    @Bean
    public RouterFunction<ServerResponse> purchaseRoutes() {
        return GatewayRouterFunctions.route("purchase-service")
                .route(RequestPredicates.path("/api/v1/purchases/**"),
                        HandlerFunctions.http("http://localhost:8084"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("purchasesServiceCircuitBreaker",
                        URI.create("forward:/purchasesFallback")))
                .build();
    }
}
