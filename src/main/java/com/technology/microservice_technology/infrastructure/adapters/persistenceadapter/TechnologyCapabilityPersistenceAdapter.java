package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter;

import com.technology.microservice_technology.domain.model.TechnologyCapability;
import com.technology.microservice_technology.domain.spi.ITechnologyCapabilityPersistencePort;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.mapper.ITechnologyCapabilityEntityMapper;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository.ITechnologyCapabilityRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class TechnologyCapabilityPersistenceAdapter implements ITechnologyCapabilityPersistencePort {
    private final ITechnologyCapabilityRepository repository;
    private final ITechnologyCapabilityEntityMapper mapper;

    @Override
    public Flux<TechnologyCapability> findByTechnologyIds(List<Long> technologyIds) {
        return repository.findByTechnologyIdIn(technologyIds)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Void> saveAll(List<TechnologyCapability> associations) {
        return Flux.fromIterable(associations)
                .map(mapper::toEntity)
                .collectList()
                .flatMapMany(repository::saveAll)
                .then();
    }

    @Override
    public Mono<Long> findCapabilityIdByTechnologyCount(int technologyCount) {
        return repository.findCapabilityIdByTechnologyCount(technologyCount);
    }

    @Override
    public Flux<TechnologyCapability> findByCapabilityId(Long capabilityId) {
        return repository.findByCapabilityId(capabilityId)
                .map(mapper::toModel);
    }
}