package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository;

import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity.TechnologyCapabilityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Collection;

@Repository
public interface ITechnologyCapabilityRepository extends ReactiveCrudRepository<TechnologyCapabilityEntity, Long> {
    Flux<TechnologyCapabilityEntity> findByTechnologyIdIn(Collection<Long> technologyIds);

    @Query("""
SELECT tc.capability_id
FROM technology_capabilities tc
GROUP BY tc.capability_id
HAVING COUNT(tc.technology_id) = :technologyCount
LIMIT 1
""")
    Mono<Long> findCapabilityIdByTechnologyCount(int technologyCount);

    Flux<TechnologyCapabilityEntity> findByCapabilityId(Long capabilityId);

}