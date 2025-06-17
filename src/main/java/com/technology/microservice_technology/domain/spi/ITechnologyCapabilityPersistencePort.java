package com.technology.microservice_technology.domain.spi;

import com.technology.microservice_technology.domain.model.TechnologyCapability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyCapabilityPersistencePort {
    Flux<TechnologyCapability> findByTechnologyIds(List<Long> technologyIds);
    Mono<Void> saveAll(List<TechnologyCapability> associations);
    Mono<Long> findCapabilityIdByTechnologyCount(int technologyCount);

}
