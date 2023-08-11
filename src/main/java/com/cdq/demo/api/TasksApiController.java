package com.cdq.demo.api;

import com.cdq.demo.task.Task;
import com.cdq.demo.task.TaskService;
import org.openapitools.api.TasksApi;
import org.openapitools.model.Error;
import org.openapitools.model.TaskCreateRequest;
import org.openapitools.model.TaskCreateResponse;
import org.openapitools.model.TaskDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class TasksApiController implements TasksApi {

    private final TaskService taskService;

    private final Validation validation;

    @Autowired
    public TasksApiController(TaskService taskService, Validation validation) {
        this.taskService = taskService;
        this.validation = validation;
    }

    @Override
    public ResponseEntity<List<TaskDetails>> tasksGet(Integer page, Integer size) {
        List<TaskDetails> tasksDetails = taskService.findTasks(page, size)
                .stream()
                .map(Mapper::map)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasksDetails);
    }

    @Override
    public ResponseEntity<TaskCreateResponse> tasksPost(TaskCreateRequest taskCreateRequest) {
        validation.validateTaskCreateRequest(taskCreateRequest);
        UUID taskUuid = taskService.createTask(taskCreateRequest.getPattern(), taskCreateRequest.getInput());
        taskService.runTask(taskUuid);
        return ResponseEntity.ok(new TaskCreateResponse().taskId(taskUuid));
    }


    @Override
    public ResponseEntity<TaskDetails> tasksTaskIdGet(UUID taskId) {
        Optional<TaskDetails> taskDetails = taskService.getTask(taskId)
                .map(Mapper::map);

        return ResponseEntity.of(taskDetails);
    }
}
