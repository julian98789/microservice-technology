package com.technology.microservice_technology.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("500","Something went wrong, please try again", ""),
    INVALID_TECHNOLOGY_NAME("400", "Invalid technology name. Must not be empty, unique, and max 50 chars.", "name"),
    INVALID_TECHNOLOGY_DESCRIPTION("400", "Invalid technology description. Must not be empty and max 90 chars.", "description"),
    TECHNOLOGY_ALREADY_EXISTS("409", "Technology name already exists.", "name"),
    TECHNOLOGY_CREATED("201", "Technology created successfully", "");

    private final String code;
    private final String message;
    private final String param;
}