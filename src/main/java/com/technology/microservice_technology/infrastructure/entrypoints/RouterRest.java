package com.technology.microservice_technology.infrastructure.entrypoints;

import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.handler.TechnologyCapabilityHandlerImpl;
import com.technology.microservice_technology.infrastructure.entrypoints.handler.TechnologyHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperation(
            path = "/api/v1/technology",
            method = {RequestMethod.POST},
            beanClass = TechnologyHandlerImpl.class,
            beanMethod = "createTechnology",
            operation = @Operation(
                    operationId = "createTechnology",
                    summary = "Creates a new technology",
                    requestBody = @RequestBody(
                            required = true,
                            content = @Content(schema = @Schema(implementation = TechnologyDTO.class))
                    ),
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Technology successfully created",
                                    content = @Content(schema = @Schema(implementation = TechnologyDTO.class))
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Invalid request"
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandlerImpl technologyHandler,
                                                         TechnologyCapabilityHandlerImpl technologyCapabilityHandler) {
        return route(POST("/technology"), technologyHandler::createTechnology)
                .andRoute(POST("/technology/capability/associate"), technologyCapabilityHandler::associateTechnologiesToCapability)
                .andRoute(GET("/technology/capability/by-count/{technologyCount}"), technologyCapabilityHandler::findCapabilityIdByTechnologyCount);

    }


}