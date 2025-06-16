package com.technology.microservice_technology.infrastructure.entrypoints.mapper;

import com.technology.microservice_technology.domain.model.Technology;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITechnologyMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Technology technologyDTOToTechnology(TechnologyDTO technologyDTO);

    TechnologyDTO technologyToDTO(Technology technology);
}