package com.technology.microservice_technology.infrastructure.entrypoints.handler;

import com.technology.microservice_technology.domain.api.ITechnologyCapabilityServicePort;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.exceptions.TechnicalException;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyCapabilityAssociationRequestDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.util.APIResponse;
import com.technology.microservice_technology.infrastructure.entrypoints.util.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TechnologyCapabilityHandlerImpl {

    private final ITechnologyCapabilityServicePort service;

    public Mono<ServerResponse> associateTechnologiesToCapability(ServerRequest request) {
        return request.bodyToMono(TechnologyCapabilityAssociationRequestDTO.class)
                .flatMap(dto -> service.associateTechnologiesToCapability(dto.getTechnologyIds(), dto.getCapabilityId()))
                .flatMap(success -> ServerResponse.ok().bodyValue("Asociaciones guardadas correctamente"))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getTechnicalMessage().getParam(),
                        ex.getTechnicalMessage().getMessage()))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        ex.getTechnicalMessage().getMessage()))
                .onErrorResume(ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        null,
                        "Error interno"));
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, String param, String message) {
        APIResponse apiErrorResponse = APIResponse
                .builder()
                .code(String.valueOf(httpStatus.value()))
                .message(message)
                .date(Instant.now().toString())
                .errors(List.of(ErrorDTO.builder().param(param).message(message).build()))
                .build();
        return ServerResponse.status(httpStatus)
                .bodyValue(apiErrorResponse);
    }
}