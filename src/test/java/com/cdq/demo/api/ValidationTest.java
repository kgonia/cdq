package com.cdq.demo.api;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.openapitools.model.TaskCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ValidationTest {

    Validation validation = new Validation();

    @Test
    void Should_NotThrowException_When_PatternLengthIsLessThanOrEqualToInputLength() {
        // Given
        TaskCreateRequest validRequest = new TaskCreateRequest()
                .input("ABCDEF")
                .pattern("ABC");

        // When & Then
        assertDoesNotThrow(() -> validation.validateTaskCreateRequest(validRequest));
    }

    @Test
    void Should_ThrowException_When_PatternLengthIsGreaterThanInputLength() {
        // Given
        TaskCreateRequest invalidRequest = new TaskCreateRequest()
                .pattern("ABCDEF")
                .input("ABC");

        // When & Then
        assertThatThrownBy(() -> validation.validateTaskCreateRequest(invalidRequest))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_REQUEST)
                .hasMessageContaining(Validation.PATTERN_LONGER_THAN_INPUT_ERROR_MESSAGE);
    }
}
