package com.technology.microservice_technology.infrastructure.entrypoints.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TechnologyCapabilityAssociationRequestDTO {
    private List<Long> technologyIds;
    private Long capabilityId;
}
