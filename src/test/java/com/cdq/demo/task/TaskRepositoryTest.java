package com.cdq.demo.task;

import com.github.f4b6a3.ulid.Ulid;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskRepositoryTest {

    private TaskRepository underTest = new TaskRepository();

    @Test
    void Should_AddTask_When_TaskIsGiven() {
        // Given
        Task task = new Task("A", "B");

        // When
        UUID taskId = underTest.addTask(task);

        // Then
        Optional<Task> retrievedTask = underTest.getTask(taskId);
        assertThat(retrievedTask).isPresent();
        assertThat(retrievedTask.get().getTaskStatus()).isEqualTo(TaskStatus.QUEUED);
    }

    @Test
    void Should_UpdateStatus_When_TaskStatusIsProvided() {
        // Given
        Task task = new Task("A", "B");
        UUID taskId = underTest.addTask(task);

        // When
        underTest.updateStatus(taskId, TaskStatus.PROCESSING);

        // Then
        Optional<Task> retrievedTask = underTest.getTask(taskId);
        assertThat(retrievedTask).isPresent();
        assertThat(retrievedTask.get().getTaskStatus()).isEqualTo(TaskStatus.PROCESSING);
    }

    @Test
    void Should_RetrieveTask_When_TaskIdIsGiven() {
        // Given
        Task task = new Task("A", "B");
        UUID taskId = underTest.addTask(task);

        // When
        Optional<Task> retrievedTask = underTest.getTask(taskId);

        // Then
        assertThat(retrievedTask).isPresent();
        assertThat(retrievedTask.get())
                .usingRecursiveComparison()
                .isEqualTo(task);
    }

    @Test
    void Should_ReturnEmptyOptional_When_NonExistentIdIsGiven() {
        // Given
        UUID nonExistentId = UUID.randomUUID();

        // When
        Optional<Task> retrievedTask = underTest.getTask(nonExistentId);

        // Then
        assertThat(retrievedTask).isNotPresent();
    }


    @Test
    void Should_ReturnSingleTask_When_OnePageIsRequested() {
        // Given
        Task task1 = new Task("A", "B");
        underTest.addTask(task1);

        Task task2 = new Task("C", "D");
        underTest.addTask(task2);
        Pageable pageable = PageRequest.of(0, 1);

        // When
        List<Task> tasks = underTest.findTasks(pageable);

        // Then
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0))
                .usingRecursiveComparison()
                .isEqualTo(task1);
    }

    @Test
    void Should_ReturnEmptyList_When_RepositoryIsEmpty() {
        // Given
        Pageable pageable = PageRequest.of(0, 1);

        // When
        List<Task> tasks = underTest.findTasks(pageable);

        // Then
        assertThat(tasks).isEmpty();
    }

    @Test
    void Should_ReturnTasksInOrderOfGeneration_When_MultipleTasksAdded() {
        // Given
        Task task1 = new Task("A", "B");
        UUID taskId1 = underTest.addTask(task1);

        Task task2 = new Task("C", "D");
        UUID taskId2 = underTest.addTask(task2);

        Pageable pageable = PageRequest.of(0, 2);

        // When
        List<Task> tasks = underTest.findTasks(pageable);

        // Then
        assertThat(tasks).hasSize(2);
        assertThat(Ulid.from(taskId1)).isLessThan(Ulid.from(taskId2));
        assertThat(tasks.get(0)).usingRecursiveComparison()
                .isEqualTo(task1);
        assertThat(tasks.get(1)).usingRecursiveComparison()
                .isEqualTo(task2);
    }


}
