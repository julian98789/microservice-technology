package com.technology.microservice_technology.domain.spi;

import com.technology.microservice_technology.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {
    Mono<Technology> save(Technology technology);
    Mono<Boolean> existsByName(String name);
    Mono<Boolean> existsById(Long id);
}
