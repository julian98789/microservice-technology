package com.technology.microservice_technology.infrastructure.entrypoints.handler;

import com.technology.microservice_technology.domain.api.ITechnologyServicePort;
import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.exceptions.TechnicalException;
import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyDTO;
import com.technology.microservice_technology.infrastructure.entrypoints.mapper.ITechnologyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyHandlerImplTest {

    @Mock
    private ITechnologyServicePort technologyServicePort;
    @Mock
    private ITechnologyMapper technologyMapper;

    private TechnologyHandlerImpl handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new TechnologyHandlerImpl(technologyServicePort, technologyMapper);
    }

    @Test
    void createTechnology_success() {
        ServerRequest request = mock(ServerRequest.class);
        TechnologyDTO dto = new TechnologyDTO(1L, "Tech", "Desc");
        Technology tech = new Technology(1L, "Tech", "Desc");

        when(request.bodyToMono(TechnologyDTO.class)).thenReturn(Mono.just(dto));
        when(technologyMapper.technologyDTOToTechnology(dto)).thenReturn(tech);
        when(technologyServicePort.registerTechnology(tech)).thenReturn(Mono.just(tech));

        ServerResponse response = handler.createTechnology(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.statusCode());
    }

    @Test
    void createTechnology_businessException() {
        ServerRequest request = mock(ServerRequest.class);
        TechnologyDTO dto = new TechnologyDTO(1L, "Tech", "Desc");
        Technology tech = new Technology(1L, "Tech", "Desc");

        when(request.bodyToMono(TechnologyDTO.class)).thenReturn(Mono.just(dto));
        when(technologyMapper.technologyDTOToTechnology(dto)).thenReturn(tech);
        when(technologyServicePort.registerTechnology(tech))
                .thenReturn(Mono.error(new BusinessException(TechnicalMessage.INVALID_TECHNOLOGY_NAME)));

        ServerResponse response = handler.createTechnology(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode());
    }

    @Test
    void createTechnology_technicalException() {
        ServerRequest request = mock(ServerRequest.class);
        TechnologyDTO dto = new TechnologyDTO(1L, "Tech", "Desc");
        Technology tech = new Technology(1L, "Tech", "Desc");

        when(request.bodyToMono(TechnologyDTO.class)).thenReturn(Mono.just(dto));
        when(technologyMapper.technologyDTOToTechnology(dto)).thenReturn(tech);
        when(technologyServicePort.registerTechnology(tech))
                .thenReturn(Mono.error(new TechnicalException(TechnicalMessage.INTERNAL_ERROR)));

        ServerResponse response = handler.createTechnology(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode());
    }

    @Test
    void createTechnology_unexpectedException() {
        ServerRequest request = mock(ServerRequest.class);
        TechnologyDTO dto = new TechnologyDTO(1L, "Tech", "Desc");
        Technology tech = new Technology(1L, "Tech", "Desc");

        when(request.bodyToMono(TechnologyDTO.class)).thenReturn(Mono.just(dto));
        when(technologyMapper.technologyDTOToTechnology(dto)).thenReturn(tech);
        when(technologyServicePort.registerTechnology(tech))
                .thenReturn(Mono.error(new RuntimeException("Unexpected")));

        ServerResponse response = handler.createTechnology(request).block();
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode());
    }
}