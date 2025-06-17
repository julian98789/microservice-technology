package com.technology.microservice_technology.infrastructure.entrypoints.dto;

import java.util.List;
import lombok.Data;

@Data
public class TechnologyCapabilityAssociationRequestDTO {
    private List<Long> technologyIds;
    private Long capabilityId;
}
