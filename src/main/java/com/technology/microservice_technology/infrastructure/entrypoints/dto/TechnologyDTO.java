package com.technology.microservice_technology.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TechnologyDTO {
    private Long id;
    private String name;
    private String description;
}