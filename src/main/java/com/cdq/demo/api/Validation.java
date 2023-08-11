package com.cdq.demo.api;

import org.openapitools.model.TaskCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class Validation {

    public static final String PATTERN_LONGER_THAN_INPUT_ERROR_MESSAGE = "Pattern length cannot be greater than input length.";

    public void validateTaskCreateRequest(TaskCreateRequest taskCreateRequest) {
        if(taskCreateRequest.getPattern().length() > taskCreateRequest.getInput().length()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,PATTERN_LONGER_THAN_INPUT_ERROR_MESSAGE);
        }
    }
}
