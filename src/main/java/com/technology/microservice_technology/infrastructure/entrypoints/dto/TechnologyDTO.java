package com.technology.microservice_technology.infrastructure.entrypoints.dto;

import lombok.Data;

@Data
public class TechnologyDTO {
    private Long id;
    private String name;
    private String description;
}