package com.perepalacin.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class KeyCloakServiceRoutes {
    @Bean
    public RouterFunction<ServerResponse> keycloakRoutes() {
        return GatewayRouterFunctions.route("keycloak-service")
                .route(RequestPredicates.POST("/realms/ecommerce-security-realm/protocol/openid-connect/token"),
                        HandlerFunctions.http("http://localhost:8091/realms/ecommerce-security-realm/protocol/openid-connect/token"))
                .build();
    }
}
