package com.cdq.demo.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskTest {

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void Should_FindBestMatch_When_GivenPatterns(String pattern, String text, int bestPosition, int minTypos) {
        Task task = new Task(pattern, text, 1);
        task.run();
        assertThat(task.getBestPosition()).isEqualTo(bestPosition);
        assertThat(task.getMinTypos()).isEqualTo(minTypos);
    }

    static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("BCD", "ABCD", 1, 0),
                Arguments.of("BWD", "ABCD", 1, 1),
                Arguments.of("CFG", "ABCDEFG", 4, 1),
                Arguments.of("ABC", "ABCABC", 0, 0),
                Arguments.of("TDD", "ABCDEFG", 1, 2),
                Arguments.of("UP", "ABCUP", 3, 0),
                Arguments.of("XYZ", "WXYWXYWXY", 1, 1), // No perfect match, same typos everywhere
                Arguments.of("ABCD", "", -1, Integer.MAX_VALUE), // Empty input
                Arguments.of("", "ABCDE", 0, 0), // Empty pattern
                Arguments.of("ABCDE", "XYZ", -1, Integer.MAX_VALUE) // Pattern longer than input
        );
    }

    @Test
    void Should_SetProgressCorrectly_When_TaskIsRun() {
        // Given
        Task task = new Task("BCD", "ABCD", 1);

        // Check initial progress
        assertThat(task.getProgressPercentage()).isEqualTo(0.0);

        // When
        task.run();

        // Then
        // Check final progress
        assertThat(task.getProgressPercentage()).isEqualTo(100.0);
    }



}
