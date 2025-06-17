package com.technology.microservice_technology.domain.usecase;

import com.technology.microservice_technology.domain.api.ITechnologyCapabilityServicePort;
import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.model.TechnologyCapability;
import com.technology.microservice_technology.domain.spi.ITechnologyCapabilityPersistencePort;
import com.technology.microservice_technology.domain.spi.ITechnologyPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TechnologyCapabilityUseCase implements ITechnologyCapabilityServicePort {

    private final ITechnologyPersistencePort technologyPersistencePort;
    private final ITechnologyCapabilityPersistencePort technologyCapabilityPersistencePort;

    public TechnologyCapabilityUseCase(ITechnologyPersistencePort technologyPersistencePort, ITechnologyCapabilityPersistencePort technologyCapabilityPersistencePort) {
        this.technologyPersistencePort = technologyPersistencePort;
        this.technologyCapabilityPersistencePort = technologyCapabilityPersistencePort;
    }

    public Mono<Boolean> associateTechnologiesToCapability(List<Long> technologyIds, Long capabilityId) {

        Set<Long> uniqueIds = new HashSet<>(technologyIds);
        if (uniqueIds.size() != technologyIds.size()) {
            return Mono.error(new BusinessException(TechnicalMessage.DUPLICATE_TECHNOLOGY_ID, "Duplicate technologyIds in request"));
        }

        return Flux.fromIterable(technologyIds)
                .flatMap(techId -> technologyPersistencePort.existsById(techId)
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(
                                new BusinessException(TechnicalMessage.TECHNOLOGY_NOT_FOUND, techId.toString()))
                        )
                )
                .then(
                        // 3. Buscar asociaciones existentes para ese capabilityId
                        technologyCapabilityPersistencePort.findByTechnologyIds(technologyIds)
                                .filter(tc -> tc.capabilityId().equals(capabilityId))
                                .map(TechnologyCapability::technologyId)
                                .collectList()
                )
                .flatMap(alreadyAssociatedIds -> {
                    if (!alreadyAssociatedIds.isEmpty()) {
                        return Mono.error(
                                new BusinessException(TechnicalMessage.TECHNOLOGY_ALREADY_ASSOCIATED, alreadyAssociatedIds.toString())
                        );
                    }
                    List<TechnologyCapability> newAssociations = technologyIds.stream()
                            .map(techId -> new TechnologyCapability(null, techId, capabilityId))
                            .toList();
                    return technologyCapabilityPersistencePort.saveAll(newAssociations)
                            .thenReturn(true);
                });
    }
}