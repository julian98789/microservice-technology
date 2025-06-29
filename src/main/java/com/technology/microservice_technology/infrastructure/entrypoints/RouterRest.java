package com.technology.microservice_technology.infrastructure.entrypoints;

import com.technology.microservice_technology.infrastructure.entrypoints.handler.TechnologyHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandlerImpl technologyHandler) {
        return route(POST("api/v1/technology"), technologyHandler::createTechnology);
    }
}