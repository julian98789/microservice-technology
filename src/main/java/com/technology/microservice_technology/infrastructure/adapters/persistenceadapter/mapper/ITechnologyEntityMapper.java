package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.mapper;

import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity.TechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITechnologyEntityMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Technology toModel(TechnologyEntity entity);
    TechnologyEntity toEntity(Technology model);
}