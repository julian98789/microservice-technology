package com.technology.microservice_technology.infrastructure.entrypoints.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.technology.microservice_technology.infrastructure.entrypoints.dto.TechnologyDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class APIResponse {
    private String code;
    private String message;
    private String identifier;
    private String date;
    private TechnologyDTO data;
    private List<ErrorDTO> errors;
}
