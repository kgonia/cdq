package com.cdq.demo.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private ThreadPoolTaskExecutor taskExecutor;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskExecutor = mock(ThreadPoolTaskExecutor.class);
        taskService = new TaskService(taskRepository, taskExecutor);
    }

    @Test
    void Should_CallFindTasksInRepository_When_FindTasksIsCalled() {
        // Given
        List<Task> tasks = Collections.singletonList(mock(Task.class));
        when(taskRepository.findTasks(any(Pageable.class))).thenReturn(tasks);

        // When
        List<Task> result = taskService.findTasks(1, 10);

        // Then
        verify(taskRepository).findTasks(PageRequest.of(1, 10));
        assertThat(result).isEqualTo(tasks);
    }

    @Test
    void Should_CreateAndAddTask_When_CreateTaskIsCalled() {
        // Given
        UUID taskId = UUID.randomUUID();
        when(taskRepository.addTask(any(Task.class))).thenReturn(taskId);

        // When
        UUID result = taskService.createTask("pattern", "input");

        // Then
        verify(taskRepository).addTask(any(Task.class));
        assertThat(result).isEqualTo(taskId);
    }

    @Test
    void Should_RunTask_When_RunTaskIsCalledWithQueuedTask() {
        // Given
        UUID taskId = UUID.randomUUID();
        Task task = mock(Task.class);
        when(task.getTaskStatus()).thenReturn(TaskStatus.QUEUED);
        when(taskRepository.getTask(taskId)).thenReturn(Optional.of(task));

        // When
        taskService.runTask(taskId);

        // Then
        verify(taskExecutor).execute(any(Runnable.class));
    }

    @Test
    void Should_CallGetTaskInRepository_When_GetTaskIsCalled() {
        // Given
        UUID taskId = UUID.randomUUID();
        Optional<Task> task = Optional.of(mock(Task.class));
        when(taskRepository.getTask(taskId)).thenReturn(task);

        // When
        Optional<Task> result = taskService.getTask(taskId);

        // Then
        verify(taskRepository).getTask(taskId);
        assertThat(result).isEqualTo(task);
    }



}
