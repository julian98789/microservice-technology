package com.technology.microservice_technology.infrastructure.entrypoints.handler;

import com.technology.microservice_technology.domain.api.ITechnologyCapabilityServicePort;
import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.exceptions.TechnicalException;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyCapabilityAssociationRequestDTO;
import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.domain.model.CapabilityRelationCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyCapabilityHandlerImplTest {

    @Mock
    private ITechnologyCapabilityServicePort service;

    private TechnologyCapabilityHandlerImpl handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new TechnologyCapabilityHandlerImpl(service);
    }

    @Test
    void associateTechnologiesToCapability_success() {
        ServerRequest request = mock(ServerRequest.class);
        TechnologyCapabilityAssociationRequestDTO dto = new TechnologyCapabilityAssociationRequestDTO(List.of(1L, 2L), 3L);

        when(request.bodyToMono(TechnologyCapabilityAssociationRequestDTO.class)).thenReturn(Mono.just(dto));
        when(service.associateTechnologiesToCapability(dto.getTechnologyIds(), dto.getCapabilityId())).thenReturn(Mono.just(true));

        ServerResponse response = handler.associateTechnologiesToCapability(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.statusCode());
    }

    @Test
    void associateTechnologiesToCapability_businessException() {
        ServerRequest request = mock(ServerRequest.class);
        TechnologyCapabilityAssociationRequestDTO dto = new TechnologyCapabilityAssociationRequestDTO(List.of(1L), 2L);

        when(request.bodyToMono(TechnologyCapabilityAssociationRequestDTO.class)).thenReturn(Mono.just(dto));
        when(service.associateTechnologiesToCapability(dto.getTechnologyIds(), dto.getCapabilityId()))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.DUPLICATE_TECHNOLOGY_ID, null)));

        ServerResponse response = handler.associateTechnologiesToCapability(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode());
    }

    @Test
    void associateTechnologiesToCapability_technicalException() {
        ServerRequest request = mock(ServerRequest.class);
        TechnologyCapabilityAssociationRequestDTO dto = new TechnologyCapabilityAssociationRequestDTO(List.of(1L), 2L);

        when(request.bodyToMono(TechnologyCapabilityAssociationRequestDTO.class)).thenReturn(Mono.just(dto));
        when(service.associateTechnologiesToCapability(dto.getTechnologyIds(), dto.getCapabilityId()))
                .thenReturn(Mono.error(new TechnicalException(TechnicalMessage.INTERNAL_ERROR)));

        ServerResponse response = handler.associateTechnologiesToCapability(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode());
    }

    @Test
    void findTechnologiesByCapabilityId_success() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("capabilityId")).thenReturn("1");
        when(service.findTechnologiesByCapabilityId(1L))
                .thenReturn(Flux.just(new Technology(1L, "Tech1", "desc")));

        ServerResponse response = handler.findTechnologiesByCapabilityId(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.statusCode());
    }

    @Test
    void getAllCapabilityRelationCounts_success() {
        ServerRequest request = mock(ServerRequest.class);
        when(service.getAllCapabilityRelationCounts())
                .thenReturn(Flux.just(new CapabilityRelationCount(1L, 5L)));

        ServerResponse response = handler.getAllCapabilityRelationCounts(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.statusCode());
    }

    @Test
    void deleteTechnologiesByCapabilityId_success() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("capabilityId")).thenReturn("1");
        when(service.deleteTechnologiesExclusivelyByCapabilityId(1L)).thenReturn(Mono.empty());

        ServerResponse response = handler.deleteTechnologiesByCapabilityId(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode());
    }

    @Test
    void deleteTechnologiesByCapabilityId_businessException() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("capabilityId")).thenReturn("1");
        when(service.deleteTechnologiesExclusivelyByCapabilityId(1L))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.DUPLICATE_TECHNOLOGY_ID, null)));

        ServerResponse response = handler.deleteTechnologiesByCapabilityId(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode());
    }

    @Test
    void deleteTechnologiesByCapabilityId_technicalException() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("capabilityId")).thenReturn("1");
        when(service.deleteTechnologiesExclusivelyByCapabilityId(1L))
                .thenReturn(Mono.error(new TechnicalException(TechnicalMessage.INTERNAL_ERROR)));

        ServerResponse response = handler.deleteTechnologiesByCapabilityId(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode());
    }
}