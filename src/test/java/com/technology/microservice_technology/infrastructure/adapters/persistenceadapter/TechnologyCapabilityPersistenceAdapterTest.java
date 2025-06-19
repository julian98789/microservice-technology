package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter;

import com.technology.microservice_technology.domain.model.CapabilityRelationCount;
import com.technology.microservice_technology.domain.model.TechnologyCapability;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity.TechnologyCapabilityEntity;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.mapper.ITechnologyCapabilityEntityMapper;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository.ITechnologyCapabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyCapabilityPersistenceAdapterTest {

    @Mock
    private ITechnologyCapabilityRepository repository;
    @Mock
    private ITechnologyCapabilityEntityMapper mapper;

    private TechnologyCapabilityPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new TechnologyCapabilityPersistenceAdapter(repository, mapper);
    }

    @Test
    void findByTechnologyIds_returnsMappedModels() {
        List<Long> ids = List.of(1L, 2L);
        TechnologyCapabilityEntity entity = new TechnologyCapabilityEntity();
        TechnologyCapability model = new TechnologyCapability(1L, 1L, 1L);

        when(repository.findByTechnologyIdIn(ids)).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        List<TechnologyCapability> result = adapter.findByTechnologyIds(ids).collectList().block();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
    }

    @Test
    void saveAll_savesEntities() {
        TechnologyCapability model = new TechnologyCapability(1L, 1L, 1L);
        TechnologyCapabilityEntity entity = new TechnologyCapabilityEntity();

        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.saveAll(ArgumentMatchers.anyList())).thenReturn(Flux.just(entity));

        Mono<Void> result = adapter.saveAll(List.of(model));
        assertDoesNotThrow(() -> result.block());
        verify(repository).saveAll(ArgumentMatchers.anyList());
    }

    @Test
    void findByCapabilityId_returnsMappedModels() {
        Long capId = 1L;
        TechnologyCapabilityEntity entity = new TechnologyCapabilityEntity();
        TechnologyCapability model = new TechnologyCapability(1L, 1L, capId);

        when(repository.findByCapabilityId(capId)).thenReturn(Flux.just(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        List<TechnologyCapability> result = adapter.findByCapabilityId(capId).collectList().block();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
    }

    @Test
    void findTechnologyIdsByCapabilityId_returnsIds() {
        Long capId = 1L;
        TechnologyCapabilityEntity entity = new TechnologyCapabilityEntity();
        entity.setTechnologyId(99L);

        when(repository.findByCapabilityId(capId)).thenReturn(Flux.just(entity));

        List<Long> ids = adapter.findTechnologyIdsByCapabilityId(capId).collectList().block();
        assertNotNull(ids);
        assertEquals(1, ids.size());
        assertEquals(99L, ids.get(0));
    }

    @Test
    void getAllCapabilityRelationCounts_returnsCounts() {
        TechnologyCapabilityEntity entity1 = new TechnologyCapabilityEntity();
        entity1.setCapabilityId(1L);
        TechnologyCapabilityEntity entity2 = new TechnologyCapabilityEntity();
        entity2.setCapabilityId(1L);
        TechnologyCapabilityEntity entity3 = new TechnologyCapabilityEntity();
        entity3.setCapabilityId(2L);

        when(repository.findAll()).thenReturn(Flux.just(entity1, entity2, entity3));

        List<CapabilityRelationCount> result = adapter.getAllCapabilityRelationCounts().collectList().block();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.capabilityId() == 1L && c.relationCount() == 2L));
        assertTrue(result.stream().anyMatch(c -> c.capabilityId() == 2L && c.relationCount() == 1L));
    }

    @Test
    void deleteByTechnologyIdAndCapabilityId_delegatesToRepository() {
        when(repository.deleteByTechnologyIdAndCapabilityId(1L, 2L)).thenReturn(Mono.empty());

        Mono<Void> result = adapter.deleteByTechnologyIdAndCapabilityId(1L, 2L);
        assertDoesNotThrow(() -> result.block());
        verify(repository).deleteByTechnologyIdAndCapabilityId(1L, 2L);
    }
}