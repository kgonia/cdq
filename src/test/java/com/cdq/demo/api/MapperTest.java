package com.cdq.demo.api;

import com.cdq.demo.task.Task;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TaskDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapperTest {

    @Test
    void Should_MapStatusOnly_When_ProgressPercentageIsNot100() {
        // Given
        Task task = mock(Task.class);
        when(task.getProgressPercentage()).thenReturn(50.0);

        // When
        TaskDetails taskDetails = Mapper.map(task);

        // Then
        assertThat(taskDetails.getStatus()).isEqualTo(50);
        assertThat(taskDetails.getPosition()).isNull();
        assertThat(taskDetails.getTypos()).isNull();
    }

    @Test
    void Should_MapAllFields_When_ProgressPercentageIs100() {
        // Given
        Task task = mock(Task.class);
        when(task.getProgressPercentage()).thenReturn(100.0);
        when(task.getBestPosition()).thenReturn(3);
        when(task.getMinTypos()).thenReturn(2);

        // When
        TaskDetails taskDetails = Mapper.map(task);

        // Then
        assertThat(taskDetails.getStatus()).isEqualTo(100);
        assertThat(taskDetails.getPosition()).isEqualTo(3);
        assertThat(taskDetails.getTypos()).isEqualTo(2);
    }
}
