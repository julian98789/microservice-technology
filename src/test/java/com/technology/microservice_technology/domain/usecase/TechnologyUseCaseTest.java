package com.technology.microservice_technology.domain.usecase;

import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.domain.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort technologyPersistencePort;

    private TechnologyUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new TechnologyUseCase(technologyPersistencePort);
    }

    @Test
    void registerTechnology_invalidName() {
        Technology tech = new Technology(1L, "", "desc");
        StepVerifier.create(useCase.registerTechnology(tech))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getTechnicalMessage() == TechnicalMessage.INVALID_TECHNOLOGY_NAME;
                })
                .verify();
    }

    @Test
    void registerTechnology_invalidDescription() {
        Technology tech = new Technology(1L, "Tech", "");
        StepVerifier.create(useCase.registerTechnology(tech))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getTechnicalMessage() == TechnicalMessage.INVALID_TECHNOLOGY_DESCRIPTION;
                })
                .verify();
    }

    @Test
    void registerTechnology_alreadyExists() {
        Technology tech = new Technology(1L, "Tech", "desc");
        when(technologyPersistencePort.existsByName("Tech")).thenReturn(Mono.just(true));
        StepVerifier.create(useCase.registerTechnology(tech))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getTechnicalMessage() == TechnicalMessage.TECHNOLOGY_ALREADY_EXISTS;
                })
                .verify();
    }

    @Test
    void registerTechnology_success() {
        Technology tech = new Technology(1L, "Tech", "desc");
        when(technologyPersistencePort.existsByName("Tech")).thenReturn(Mono.just(false));
        when(technologyPersistencePort.save(any())).thenReturn(Mono.just(tech));
        StepVerifier.create(useCase.registerTechnology(tech))
                .expectNext(tech)
                .verifyComplete();
    }
}