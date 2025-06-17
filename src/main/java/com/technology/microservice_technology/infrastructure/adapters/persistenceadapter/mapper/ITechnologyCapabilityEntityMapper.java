package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.mapper;

import com.technology.microservice_technology.domain.model.TechnologyCapability;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity.TechnologyCapabilityEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ITechnologyCapabilityEntityMapper {
    TechnologyCapability toModel(TechnologyCapabilityEntity entity);
    TechnologyCapabilityEntity toEntity(TechnologyCapability model);
}