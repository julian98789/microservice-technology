package com.technology.microservice_technology.config;


import com.technology.microservice_technology.domain.api.ITechnologyServicePort;
import com.technology.microservice_technology.domain.spi.ITechnologyPersistencePort;
import com.technology.microservice_technology.domain.usecase.TechnologyUseCase;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.TechnologyPersistenceAdapter;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.mapper.ITechnologyEntityMapper;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.repository.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {
        private final ITechnologyRepository technologyRepository;
        private final ITechnologyEntityMapper technologyEntityMapper;

        @Bean
        public ITechnologyPersistencePort technologyPersistencePort() {
                return new TechnologyPersistenceAdapter(technologyRepository, technologyEntityMapper);
        }

        @Bean
        public ITechnologyServicePort technologyServicePort(ITechnologyPersistencePort technologyPersistencePort){
                return new TechnologyUseCase(technologyPersistencePort);
        }
}