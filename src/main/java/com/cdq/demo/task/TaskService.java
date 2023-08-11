package com.cdq.demo.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public TaskService(TaskRepository taskRepository, ThreadPoolTaskExecutor taskExecutor) {
        this.taskRepository = taskRepository;
        this.taskExecutor = taskExecutor;
    }

    public List<Task> findTasks(Integer page, Integer size) {
        return taskRepository.findTasks(PageRequest.of(page, size));
    }

    public UUID createTask(String pattern, String input) {
        Task task = new Task(pattern, input);
        return taskRepository.addTask(task);
    }

    public void runTask(UUID taskUuid) {
        getTask(taskUuid)
                .filter(task -> TaskStatus.QUEUED.equals(task.getTaskStatus()))
                .ifPresent(task ->
                        taskExecutor.execute(() -> {
                            taskRepository.updateStatus(taskUuid, TaskStatus.PROCESSING);
                            task.run();
                            taskRepository.updateStatus(taskUuid, TaskStatus.COMPLETED);}));

    }

    public Optional<Task> getTask(UUID taskId) {
        return taskRepository.getTask(taskId);
    }
}
