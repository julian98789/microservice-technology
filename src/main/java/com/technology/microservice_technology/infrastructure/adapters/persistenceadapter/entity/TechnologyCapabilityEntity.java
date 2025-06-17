package com.technology.microservice_technology.infrastructure.adapters.persistenceadapter.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "technology_capabilities")
@Getter
@Setter
@RequiredArgsConstructor
public class TechnologyCapabilityEntity {
    @Id
    private Long id;
    private Long technologyId;
    private Long capabilityId;
}
