package com.technology.microservice_technology.domain.exceptions;


import com.technology.microservice_technology.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProcessorException {

    private final String paramValue;

    public BusinessException(TechnicalMessage technicalMessage, String paramValue) {
        super(technicalMessage.getMessage(), technicalMessage);
        this.paramValue = paramValue;
    }

    public BusinessException(TechnicalMessage technicalMessage) {
        this(technicalMessage, technicalMessage.getParam());
    }



}
