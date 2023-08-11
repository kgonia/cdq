package com.cdq.demo.task;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;


/**
 * Repository for managing tasks.
 * <p>
 * Key Aspects:
 * <ul>
 *     <li>Uses {@link Ulid} as a key which ensures tasks are sorted by their generation time.</li>
 *     <li>Tasks are stored in a {@link ConcurrentSkipListMap}, ensuring thread-safe operations and a natural ordering by the ULID.</li>
 *     <li>Methods return {@link Optional} for robust error handling, especially when tasks are not found.</li>
 *     <li>Provides paginated access to tasks with the {@code findTasks} method.</li>
 * </ul>
 * </p>
 */
@Component
class TaskRepository {
    private final Map<Ulid, Task> tasks = new ConcurrentSkipListMap<>();

    public UUID addTask(Task task) {
        task.setTaskStatus(TaskStatus.QUEUED);
        Ulid ulid = UlidCreator.getMonotonicUlid();
        tasks.put(ulid, task);
        return ulid.toUuid();
    }

    public void updateStatus(UUID taskId, TaskStatus status) {
        getTask(taskId).ifPresent(task -> task.setTaskStatus(status));
    }

    public Optional<Task> getTask(UUID taskId) {
        return Optional.ofNullable(tasks.get(Ulid.from(taskId)));
    }

    public List<Task> findTasks(Pageable pageable) {
        return tasks.values().stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }
}
