package com.technology.microservice_technology.domain.usecase;

import com.technology.microservice_technology.domain.api.ITechnologyCapabilityServicePort;
import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.model.Technology;
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

    public TechnologyCapabilityUseCase(ITechnologyPersistencePort technologyPersistencePort,
                                       ITechnologyCapabilityPersistencePort technologyCapabilityPersistencePort) {
        this.technologyPersistencePort = technologyPersistencePort;
        this.technologyCapabilityPersistencePort = technologyCapabilityPersistencePort;
    }

    @Override
    public Mono<Boolean> associateTechnologiesToCapability(List<Long> technologyIds, Long capabilityId) {
        return validateNoDuplicates(technologyIds)
                .then(validateTechnologiesExist(technologyIds))
                .then(checkAlreadyAssociated(technologyIds, capabilityId))
                .then(validateLimitNotExceeded(technologyIds, capabilityId))
                .flatMap(newAssociations -> technologyCapabilityPersistencePort.saveAll(newAssociations).thenReturn(true));
    }

    private Mono<Void> validateNoDuplicates(List<Long> technologyIds) {
        Set<Long> uniqueIds = new HashSet<>();
        Set<Long> duplicatedIds = new HashSet<>();
        for (Long id : technologyIds) {
            if (!uniqueIds.add(id)) {
                duplicatedIds.add(id);
            }
        }
        if (!duplicatedIds.isEmpty()) {
            return Mono.error(new BusinessException(
                    TechnicalMessage.DUPLICATE_TECHNOLOGY_ID

            ));
        }
        return Mono.empty();
    }

    private Mono<Void> validateTechnologiesExist(List<Long> technologyIds) {
        return Flux.fromIterable(technologyIds)
                .flatMap(techId -> technologyPersistencePort.existsById(techId)
                        .flatMap(exists -> {
                            if (!exists) {
                                return Mono.error(new BusinessException(
                                        TechnicalMessage.TECHNOLOGY_NOT_FOUND

                                ));
                            }
                            return Mono.just(true);
                        })
                )
                .then();
    }

    private Mono<Void> checkAlreadyAssociated(List<Long> technologyIds, Long capabilityId) {
        return technologyCapabilityPersistencePort.findByTechnologyIds(technologyIds)
                .filter(tc -> tc.capabilityId().equals(capabilityId))
                .map(TechnologyCapability::technologyId)
                .collectList()
                .flatMap(alreadyAssociatedIds -> {
                    if (!alreadyAssociatedIds.isEmpty()) {
                        return Mono.error(
                                new BusinessException(
                                        TechnicalMessage.TECHNOLOGY_ALREADY_ASSOCIATED

                                )
                        );
                    }
                    return Mono.empty();
                });
    }

    private Mono<List<TechnologyCapability>> validateLimitNotExceeded(List<Long> technologyIds, Long capabilityId) {
        return technologyCapabilityPersistencePort.findByCapabilityId(capabilityId)
                .count()
                .flatMap(currentCount -> {
                    long total = currentCount + technologyIds.size();
                    if (total > 20) {
                        return Mono.error(new BusinessException(
                                TechnicalMessage.CAPABILITY_TECHNOLOGY_LIMIT
                        ));
                    }
                    List<TechnologyCapability> newAssociations = technologyIds.stream()
                            .map(techId -> new TechnologyCapability(null, techId, capabilityId))
                            .toList();
                    return Mono.just(newAssociations);
                });
    }

    @Override
    public Mono<Long> findCapabilityIdByTechnologyCount(int technologyCount) {
        return technologyCapabilityPersistencePort.findCapabilityIdByTechnologyCount(technologyCount);
    }

    @Override
    public Flux<Technology> findTechnologiesByCapabilityId(Long capabilityId) {
        return technologyCapabilityPersistencePort.findTechnologyIdsByCapabilityId(capabilityId)
                .flatMap(technologyPersistencePort::findById);
    }
}