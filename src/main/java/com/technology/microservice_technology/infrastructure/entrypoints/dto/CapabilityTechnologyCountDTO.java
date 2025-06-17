package com.technology.microservice_technology.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CapabilityTechnologyCountDTO {
    private Long capabilityId;
    private Long technologyCount;
}