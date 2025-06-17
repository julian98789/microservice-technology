package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository;

import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity.TechnologyCapabilityEntity;

import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository.interfaceprojection.CapabilityTechnologyCountProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


import java.util.Collection;

@Repository
public interface ITechnologyCapabilityRepository extends ReactiveCrudRepository<TechnologyCapabilityEntity, Long> {
    Flux<TechnologyCapabilityEntity> findByTechnologyIdIn(Collection<Long> technologyIds);

    Flux<TechnologyCapabilityEntity> findByCapabilityId(Long capabilityId);

    Flux<TechnologyCapabilityEntity> findAll();
}