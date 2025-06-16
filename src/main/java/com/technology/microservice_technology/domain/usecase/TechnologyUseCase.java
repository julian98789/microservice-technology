package com.technology.microservice_technology.domain.usecase;


import com.technology.microservice_technology.domain.api.ITechnologyServicePort;
import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import com.technology.microservice_technology.domain.exceptions.BusinessException;
import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.domain.spi.ITechnologyPersistencePort;
import reactor.core.publisher.Mono;

public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort technologyPersistencePort;

    public TechnologyUseCase(ITechnologyPersistencePort technologyPersistencePort) {
        this.technologyPersistencePort = technologyPersistencePort;
    }

    @Override
    public Mono<Technology> registerTechnology(Technology technology) {

        if (technology.name() == null || technology.name().isBlank() || technology.name().length() > 50) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_TECHNOLOGY_NAME));
        }
        if (technology.description() == null || technology.description().isBlank() || technology.description().length() > 90) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_TECHNOLOGY_DESCRIPTION));
        }
        return technologyPersistencePort.existsByName(technology.name())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(TechnicalMessage.TECHNOLOGY_ALREADY_EXISTS));
                    }
                    return technologyPersistencePort.save(technology);
                });
    }
}
