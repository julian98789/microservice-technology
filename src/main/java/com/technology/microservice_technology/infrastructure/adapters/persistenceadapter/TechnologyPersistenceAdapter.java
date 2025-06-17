package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter;

import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.domain.spi.ITechnologyPersistencePort;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity.TechnologyEntity;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.mapper.ITechnologyEntityMapper;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository.ITechnologyRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class TechnologyPersistenceAdapter implements ITechnologyPersistencePort {
    private final ITechnologyRepository technologyRepository;
    private final ITechnologyEntityMapper technologyEntityMapper;

    @Override
    public Mono<Technology> save(Technology technology) {
        TechnologyEntity entity = technologyEntityMapper.toEntity(technology);
        return technologyRepository.save(entity)
                .map(technologyEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return technologyRepository.findByName(name)
                .map(technologyEntityMapper::toModel)
                .map(tech -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return technologyRepository.existsById(id);
    }
}