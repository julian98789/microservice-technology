package com.technology.microservice_technology.domain.usecase;

import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.model.CapabilityRelationCount;
import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.domain.model.TechnologyCapability;
import com.technology.microservice_technology.domain.spi.ITechnologyCapabilityPersistencePort;
import com.technology.microservice_technology.domain.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyCapabilityUseCaseTest {

    @Mock
    private ITechnologyPersistencePort technologyPersistencePort;

    @Mock
    private ITechnologyCapabilityPersistencePort technologyCapabilityPersistencePort;

    private TechnologyCapabilityUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new TechnologyCapabilityUseCase(technologyPersistencePort, technologyCapabilityPersistencePort);
    }

    @Test
    void associateTechnologiesToCapability_success() {
        List<Long> techIds = List.of(1L, 2L);
        Long capId = 10L;

        when(technologyPersistencePort.existsById(anyLong())).thenReturn(Mono.just(true));
        when(technologyCapabilityPersistencePort.findByTechnologyIds(anyList())).thenReturn(Flux.empty());
        when(technologyCapabilityPersistencePort.findByCapabilityId(anyLong())).thenReturn(Flux.empty());
        when(technologyCapabilityPersistencePort.saveAll(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.associateTechnologiesToCapability(techIds, capId))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void associateTechnologiesToCapability_duplicateIds() {
        List<Long> techIds = List.of(1L, 1L);
        Long capId = 10L;

        when(technologyCapabilityPersistencePort.findByTechnologyIds(anyList())).thenReturn(Flux.empty());
        when(technologyCapabilityPersistencePort.findByCapabilityId(anyLong())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.associateTechnologiesToCapability(techIds, capId))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getTechnicalMessage() == TechnicalMessage.DUPLICATE_TECHNOLOGY_ID;
                })
                .verify();
    }

    @Test
    void associateTechnologiesToCapability_technologyNotFound() {
        List<Long> techIds = List.of(1L, 2L);
        Long capId = 10L;

        when(technologyPersistencePort.existsById(1L)).thenReturn(Mono.just(true));
        when(technologyPersistencePort.existsById(2L)).thenReturn(Mono.just(false));
        when(technologyCapabilityPersistencePort.findByTechnologyIds(anyList())).thenReturn(Flux.empty());
        when(technologyCapabilityPersistencePort.findByCapabilityId(anyLong())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.associateTechnologiesToCapability(techIds, capId))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getTechnicalMessage() == TechnicalMessage.TECHNOLOGY_NOT_FOUND;
                })
                .verify();
    }

    @Test
    void associateTechnologiesToCapability_alreadyAssociated() {
        List<Long> techIds = List.of(1L);
        Long capId = 10L;

        when(technologyPersistencePort.existsById(anyLong())).thenReturn(Mono.just(true));
        when(technologyCapabilityPersistencePort.findByTechnologyIds(anyList()))
                .thenReturn(Flux.just(new TechnologyCapability(100L, 1L, 10L)));
        when(technologyCapabilityPersistencePort.findByCapabilityId(anyLong())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.associateTechnologiesToCapability(techIds, capId))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getTechnicalMessage() == TechnicalMessage.TECHNOLOGY_ALREADY_ASSOCIATED;
                })
                .verify();
    }

    @Test
    void associateTechnologiesToCapability_limitExceeded() {
        List<Long> techIds = List.of(1L, 2L);
        Long capId = 10L;

        when(technologyPersistencePort.existsById(anyLong())).thenReturn(Mono.just(true));
        when(technologyCapabilityPersistencePort.findByTechnologyIds(anyList())).thenReturn(Flux.empty());
        when(technologyCapabilityPersistencePort.findByCapabilityId(anyLong()))
                .thenReturn(Flux.range(0, 19).map(i -> new TechnologyCapability((long) i, (long) i, capId)));

        StepVerifier.create(useCase.associateTechnologiesToCapability(techIds, capId))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    assert ((BusinessException) e).getTechnicalMessage() == TechnicalMessage.CAPABILITY_TECHNOLOGY_LIMIT;
                })
                .verify();
    }

    @Test
    void findTechnologiesByCapabilityId_success() {
        Long capId = 10L;
        when(technologyCapabilityPersistencePort.findTechnologyIdsByCapabilityId(capId)).thenReturn(Flux.just(1L, 2L));
        when(technologyPersistencePort.findById(1L)).thenReturn(Mono.just(new Technology(1L, "Tech1", "Description1")));
        when(technologyPersistencePort.findById(2L)).thenReturn(Mono.just(new Technology(2L, "Tech2", "Description1")));

        StepVerifier.create(useCase.findTechnologiesByCapabilityId(capId))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void deleteTechnologiesExclusivelyByCapabilityId_success() {
        Long capId = 10L;

        TechnologyCapability tc1 = new TechnologyCapability(1L, 100L, capId);
        TechnologyCapability tc2 = new TechnologyCapability(2L, 200L, capId);

        when(technologyCapabilityPersistencePort.findByCapabilityId(capId))
                .thenReturn(Flux.just(tc1, tc2));
        when(technologyCapabilityPersistencePort.findByTechnologyIds(anyList()))
                .thenAnswer(invocation -> {
                    List<Long> ids = invocation.getArgument(0);
                    return Flux.fromIterable(ids).map(id -> {
                        if (id.equals(100L)) return tc1;
                        else return tc2;
                    });
                });
        when(technologyPersistencePort.deleteById(anyLong())).thenReturn(Mono.empty());
        when(technologyCapabilityPersistencePort.deleteByTechnologyIdAndCapabilityId(anyLong(), eq(capId)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteTechnologiesExclusivelyByCapabilityId(capId))
                .verifyComplete();

        verify(technologyPersistencePort, times(2)).deleteById(anyLong());
        verify(technologyCapabilityPersistencePort, times(2)).deleteByTechnologyIdAndCapabilityId(anyLong(), eq(capId));
    }

    @Test
    void getAllCapabilityRelationCounts_success() {
        CapabilityRelationCount count1 = new CapabilityRelationCount(1L, 5L);
        CapabilityRelationCount count2 = new CapabilityRelationCount(2L, 3L);

        when(technologyCapabilityPersistencePort.getAllCapabilityRelationCounts())
                .thenReturn(Flux.just(count1, count2));

        StepVerifier.create(useCase.getAllCapabilityRelationCounts())
                .expectNext(count1)
                .expectNext(count2)
                .verifyComplete();

        verify(technologyCapabilityPersistencePort).getAllCapabilityRelationCounts();
    }
}