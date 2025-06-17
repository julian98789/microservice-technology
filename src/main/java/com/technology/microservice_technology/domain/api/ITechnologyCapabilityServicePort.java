package com.technology.microservice_technology.domain.api;


import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyCapabilityServicePort {
    Mono<Boolean> associateTechnologiesToCapability(List<Long> technologyIds, Long capabilityId);
    Mono<Long> findCapabilityIdByTechnologyCount(int technologyCount);

}