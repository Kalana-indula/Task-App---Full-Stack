package com.todo.backend.util;

import com.todo.backend.repository.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class TaskIdGenerator {

    private final TaskRepository taskRepository;

    public TaskIdGenerator(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public String generate() {
        Long lastId = taskRepository.findTopByOrderByIdDesc()
                .map(task -> task.getId())
                .orElse(0L);

        Long nextId = lastId + 1;
        return "TSK " + nextId;
    }
}
