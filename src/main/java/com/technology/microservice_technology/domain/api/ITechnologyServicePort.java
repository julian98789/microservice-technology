package com.technology.microservice_technology.domain.api;

import com.technology.microservice_technology.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {
    Mono<Technology> registerTechnology(Technology technology);
}
