package com.technology.microservice_technology.infrastructure.entrypoints;

import com.technology.microservice_technology.infrastructure.entrypoints.dto.CapabilityTechnologyCountDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyCapabilityAssociationRequestDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologySummaryDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.handler.TechnologyCapabilityHandlerImpl;
import com.technology.microservice_technology.infrastructure.entrypoints.handler.TechnologyHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/technology",
                    method = RequestMethod.POST,
                    beanClass = TechnologyHandlerImpl.class,
                    beanMethod = "createTechnology",
                    operation = @Operation(
                            operationId = "createTechnology",
                            summary = "Create a new technology",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = TechnologyDTO.class),
                                            examples = @ExampleObject(
                                                    name = "TechnologyDTO Example",
                                                    summary = "Technology data structure",
                                                    value = "{\n  \"name\": \"string\",\n  \"description\": \"string\"\n}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Technology created",
                                            content = @Content(
                                                    schema = @Schema(implementation = TechnologyDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "TechnologyDTO Response Example",
                                                            summary = "Technology response structure",
                                                            value = "{\n  \"id\": 1,\n  \"name\": \"string\",\n  \"description\": \"string\"\n}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Invalid request")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/technology/capability/associate",
                    method = RequestMethod.POST,
                    beanClass = TechnologyCapabilityHandlerImpl.class,
                    beanMethod = "associateTechnologiesToCapability",
                    operation = @Operation(
                            operationId = "associateTechnologiesToCapability",
                            summary = "Associate technologies to a capability",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = TechnologyCapabilityAssociationRequestDTO.class),
                                            examples = @ExampleObject(
                                                    name = "AssociationRequest Example",
                                                    summary = "Association request structure",
                                                    value = "{\n  \"capabilityId\": 1,\n  \"technologyIds\": [1, 2]\n}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Association successful"
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Invalid request")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/technology/capability/{capabilityId}/technologies",
                    method = RequestMethod.GET,
                    beanClass = TechnologyCapabilityHandlerImpl.class,
                    beanMethod = "findTechnologiesByCapabilityId",
                    operation = @Operation(
                            operationId = "findTechnologiesByCapabilityId",
                            summary = "Get technologies by capability ID",
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "capabilityId",
                                            description = "Capability ID",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of technologies",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = TechnologySummaryDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "Technologies List Example",
                                                            summary = "List of technology structures",
                                                            value = "[{\n  \"id\": 1,\n  \"name\": \"string\"\n}]"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(responseCode = "404", description = "Capability not found")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/technology/capability/relation-counts",
                    method = RequestMethod.GET,
                    beanClass = TechnologyCapabilityHandlerImpl.class,
                    beanMethod = "getAllCapabilityRelationCounts",
                    operation = @Operation(
                            operationId = "getAllCapabilityRelationCounts",
                            summary = "Get capability relation counts",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Counts retrieved",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = CapabilityTechnologyCountDTO.class),
                                                    examples = @ExampleObject(
                                                            name = "Relation Counts Example",
                                                            summary = "Relation counts structure",
                                                            value = "[{\n  \"capabilityId\": 1,\n  \"technologyCount\": 2\n}]"
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/technology/capability/{capabilityId}/exclusive-delete",
                    method = RequestMethod.DELETE,
                    beanClass = TechnologyCapabilityHandlerImpl.class,
                    beanMethod = "deleteTechnologiesByCapabilityId",
                    operation = @Operation(
                            operationId = "deleteTechnologiesByCapabilityId",
                            summary = "Delete technologies by capability ID",
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "capabilityId",
                                            description = "Capability ID",
                                            required = true
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Deletion successful"),
                                    @ApiResponse(responseCode = "404", description = "Capability not found")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandlerImpl technologyHandler,
                                                         TechnologyCapabilityHandlerImpl technologyCapabilityHandler) {
        return route(POST("/technology"), technologyHandler::createTechnology)
                .andRoute(POST("/technology/capability/associate"),
                        technologyCapabilityHandler::associateTechnologiesToCapability)
                .andRoute(GET("/technology/capability/{capabilityId}/technologies"),
                        technologyCapabilityHandler::findTechnologiesByCapabilityId)
                .andRoute(
                        GET("/technology/capability/relation-counts"),
                        technologyCapabilityHandler::getAllCapabilityRelationCounts)
                .andRoute(DELETE("/technology/capability/{capabilityId}/exclusive-delete"),
                        technologyCapabilityHandler::deleteTechnologiesByCapabilityId);

    }

}