package com.technology.microservice_technology.infrastructure.entrypoints.handler;

import com.technology.microservice_technology.domain.api.ITechnologyCapabilityServicePort;
import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.exceptions.TechnicalException;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.CapabilityTechnologyCountDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyCapabilityAssociationRequestDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologySummaryDTO;
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
                .flatMap(dto -> service.associateTechnologiesToCapability(
                        dto.getTechnologyIds(), dto.getCapabilityId()))
                .flatMap(success -> ServerResponse.ok().bodyValue(TechnicalMessage.SAVED_ASSOCIATION.getMessage()))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getTechnicalMessage(),
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .param(ex.getTechnicalMessage().getParam())
                                .build())))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        TechnicalMessage.INTERNAL_ERROR,
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .param(ex.getTechnicalMessage().getParam())
                                .build())))
                .onErrorResume(ex -> {
                    log.error("Unexpected error occurred", ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            TechnicalMessage.INTERNAL_ERROR,
                            List.of(ErrorDTO.builder()
                                    .code(TechnicalMessage.INTERNAL_ERROR.getCode())
                                    .message(TechnicalMessage.INTERNAL_ERROR.getMessage())
                                    .build()));
                });
    }


    public Mono<ServerResponse> findTechnologiesByCapabilityId(ServerRequest request) {
        Long capabilityId = Long.parseLong(request.pathVariable("capabilityId"));
        return service.findTechnologiesByCapabilityId(capabilityId)
                .map(tech -> new TechnologySummaryDTO(tech.id(), tech.name()))
                .collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(list));
    }

    public Mono<ServerResponse> getAllCapabilityRelationCounts(ServerRequest request) {
        return service.getAllCapabilityRelationCounts()
                .map(crc -> new CapabilityTechnologyCountDTO(crc.capabilityId(), crc.relationCount()))
                .collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(list));
    }

    public Mono<ServerResponse> deleteTechnologiesByCapabilityId(ServerRequest request) {
        Long capabilityId = Long.parseLong(request.pathVariable("capabilityId"));
        return service
                .deleteTechnologiesExclusivelyByCapabilityId(capabilityId)
                .then(ServerResponse.noContent().build())
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getTechnicalMessage(),
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .param(ex.getTechnicalMessage().getParam())
                                .build())))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        TechnicalMessage.INTERNAL_ERROR,
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .param(ex.getTechnicalMessage().getParam())
                                .build())))
                .onErrorResume(ex -> {
                    log.error("Unexpected error occurred", ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            TechnicalMessage.INTERNAL_ERROR,
                            List.of(ErrorDTO.builder()
                                    .code(TechnicalMessage.INTERNAL_ERROR.getCode())
                                    .message(TechnicalMessage.INTERNAL_ERROR.getMessage())
                                    .build()));
                });
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus,  TechnicalMessage error,
                                                    List<ErrorDTO> errors) {
        return Mono.defer(() -> {
            APIResponse apiErrorResponse = APIResponse
                    .builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .date(Instant.now().toString())
                    .errors(errors)
                    .build();
            return ServerResponse.status(httpStatus)
                    .bodyValue(apiErrorResponse);
        });
    }
}