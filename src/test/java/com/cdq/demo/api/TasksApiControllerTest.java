package com.cdq.demo.api;

import com.cdq.demo.task.Task;
import com.cdq.demo.task.TaskService;
import com.cdq.demo.task.TaskServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TaskCreateRequest;
import org.openapitools.model.TaskCreateResponse;
import org.openapitools.model.TaskDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TasksApiControllerTest {

    private TaskService taskService;
    private Validation validation;
    private TasksApiController controller;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        validation = mock(Validation.class);
        controller = new TasksApiController(taskService, validation);
    }

    @Test
    void Should_CallFindTasks_When_GettingTasks() {
        // Given
        List<Task> taskList = Collections.emptyList();
        when(taskService.findTasks(anyInt(), anyInt())).thenReturn(taskList);

        // When
        ResponseEntity<List<TaskDetails>> response = controller.tasksGet(1, 10);

        // Then
        verify(taskService).findTasks(1, 10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void Should_CallValidationAndCreateTask_When_CreatingTask() {
        // Given
        UUID taskId = UUID.randomUUID();
        TaskCreateRequest request = new TaskCreateRequest()
                .input("ABCDEF")
                .pattern("ABC");
        when(taskService.createTask(anyString(), anyString())).thenReturn(taskId);

        // When
        ResponseEntity<TaskCreateResponse> response = controller.tasksPost(request);

        // Then
        verify(validation).validateTaskCreateRequest(request);
        verify(taskService).createTask(request.getPattern(), request.getInput());
        verify(taskService).runTask(taskId);
        assertThat(response.getBody().getTaskId()).isEqualTo(taskId);
    }

    @Test
    void Should_CallGetTask_When_GettingTaskById() {
        // Given
        UUID taskId = UUID.randomUUID();
        Task task = mock(Task.class);
        TaskDetails taskDetails = Mapper.map(task);
        when(taskService.getTask(taskId)).thenReturn(Optional.of(task));

        // When
        ResponseEntity<TaskDetails> response = controller.tasksTaskIdGet(taskId);

        // Then
        verify(taskService).getTask(taskId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingRecursiveComparison()
                .isEqualTo(taskDetails);
    }

    @Test
    void Should_ReturnNotFound_When_TaskIdDoesNotExist() {
        // Given
        UUID taskId = UUID.randomUUID();
        when(taskService.getTask(taskId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<TaskDetails> response = controller.tasksTaskIdGet(taskId);

        // Then
        verify(taskService).getTask(taskId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}