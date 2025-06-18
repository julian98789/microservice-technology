package com.technology.microservice_technology.domain.spi;

import com.technology.microservice_technology.domain.model.CapabilityRelationCount;
import com.technology.microservice_technology.domain.model.TechnologyCapability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyCapabilityPersistencePort {
    Flux<TechnologyCapability> findByTechnologyIds(List<Long> technologyIds);
    Mono<Void> saveAll(List<TechnologyCapability> associations);
    Flux<TechnologyCapability> findByCapabilityId(Long capabilityId);
    Flux<Long> findTechnologyIdsByCapabilityId(Long capabilityId);
    Flux<CapabilityRelationCount> getAllCapabilityRelationCounts();
    Mono<Void> deleteByTechnologyIdAndCapabilityId(Long technologyId, Long capabilityId);

}
