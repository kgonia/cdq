package com.cdq.demo.api;

import com.cdq.demo.task.Task;
import org.openapitools.model.TaskDetails;

public final class Mapper {

    private Mapper() {
    }

    static TaskDetails map(Task task) {
        if ((int) task.getProgressPercentage() != 100) {
            return new TaskDetails()
                    .status((int) task.getProgressPercentage());
        } else {
            return new TaskDetails()
                    .position(task.getBestPosition())
                    .typos(task.getMinTypos())
                    .status((int) task.getProgressPercentage());
        }
    }
}
