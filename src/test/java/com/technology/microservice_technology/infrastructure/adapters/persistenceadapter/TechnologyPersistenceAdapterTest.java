package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter;

import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity.TechnologyEntity;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.mapper.ITechnologyEntityMapper;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository.ITechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TechnologyPersistenceAdapterTest {

    @Mock
    private ITechnologyRepository technologyRepository;
    @Mock
    private ITechnologyEntityMapper technologyEntityMapper;

    private TechnologyPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new TechnologyPersistenceAdapter(technologyRepository, technologyEntityMapper);
    }

    @Test
    void save_mapsAndSavesEntity() {
        Technology tech = new Technology(1L, "Java", "Desc");
        TechnologyEntity entity = new TechnologyEntity();
        TechnologyEntity savedEntity = new TechnologyEntity();

        when(technologyEntityMapper.toEntity(tech)).thenReturn(entity);
        when(technologyRepository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(technologyEntityMapper.toModel(savedEntity)).thenReturn(tech);

        Technology result = adapter.save(tech).block();
        assertNotNull(result);
        assertEquals(tech, result);
    }

    @Test
    void existsByName_returnsTrueIfExists() {
        String name = "Java";
        TechnologyEntity entity = new TechnologyEntity();
        Technology tech = new Technology(1L, name, "Desc");

        when(technologyRepository.findByName(name)).thenReturn(Mono.just(entity));
        when(technologyEntityMapper.toModel(entity)).thenReturn(tech);

        Boolean exists = adapter.existsByName(name).block();
        assertTrue(exists);
    }

    @Test
    void existsByName_returnsFalseIfNotExists() {
        String name = "Java";
        when(technologyRepository.findByName(name)).thenReturn(Mono.empty());

        Boolean exists = adapter.existsByName(name).block();
        assertFalse(exists);
    }

    @Test
    void existsById_delegatesToRepository() {
        when(technologyRepository.existsById(1L)).thenReturn(Mono.just(true));
        Boolean exists = adapter.existsById(1L).block();
        assertTrue(exists);
    }

    @Test
    void findById_mapsEntityToModel() {
        Long id = 1L;
        TechnologyEntity entity = new TechnologyEntity();
        Technology tech = new Technology(id, "Java", "Desc");

        when(technologyRepository.findById(id)).thenReturn(Mono.just(entity));
        when(technologyEntityMapper.toModel(entity)).thenReturn(tech);

        Technology result = adapter.findById(id).block();
        assertNotNull(result);
        assertEquals(tech, result);
    }

    @Test
    void deleteById_delegatesToRepository() {
        when(technologyRepository.deleteById(1L)).thenReturn(Mono.empty());
        assertDoesNotThrow(() -> adapter.deleteById(1L).block());
        verify(technologyRepository).deleteById(1L);
    }
}