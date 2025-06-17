package com.technology.microservice_technology.domain.api;


import com.technology.microservice_technology.domain.model.CapabilityRelationCount;
import com.technology.microservice_technology.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyCapabilityServicePort {
    Mono<Boolean> associateTechnologiesToCapability(List<Long> technologyIds, Long capabilityId);
    Flux<Technology> findTechnologiesByCapabilityId(Long capabilityId);
    Flux<CapabilityRelationCount> getAllCapabilityRelationCounts();
}