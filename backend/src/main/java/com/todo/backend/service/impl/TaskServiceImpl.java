package com.todo.backend.service.impl;

import com.todo.backend.dto.AddTaskDto;
import com.todo.backend.dto.CompleteResponse;
import com.todo.backend.dto.ListResponse;
import com.todo.backend.entity.Task;
import com.todo.backend.exception.TaskNotFoundException;
import com.todo.backend.repository.TaskRepository;
import com.todo.backend.service.TaskService;
import com.todo.backend.util.TaskIdGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskIdGenerator taskIdGenerator;

    public TaskServiceImpl(TaskRepository taskRepository, TaskIdGenerator taskIdGenerator) {
        this.taskRepository = taskRepository;
        this.taskIdGenerator = taskIdGenerator;
    }

    @Override
    public Task createTask(AddTaskDto addTaskDto) {

        Task task = Task.builder()
                .title(addTaskDto.getTitle())
                .description(addTaskDto.getDescription())
                .completed(false)
                .createdAt(LocalDateTime.now())
                .taskId(taskIdGenerator.generate())
                .build();

        return taskRepository.save(task);
    }

    // find recent five tasks
    @Override
    public ListResponse<Task> findRecentTasks() {

        List<Task> tasks = taskRepository.findRecentTasks();

        ListResponse<Task> response = new ListResponse<>();
        response.setEntityList(tasks);

        if (tasks.isEmpty()) {
            response.setMessage("No tasks found");
        } else {
            response.setMessage("No of tasks found : " + tasks.size());
        }

        return response;
    }

    @Override
    public CompleteResponse completeTask(Long id) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task " + id + " not found"));

        existingTask.setCompleted(true);
        Task updatedTask = taskRepository.save(existingTask);

        CompleteResponse response = new CompleteResponse();
        response.setMessage("Task has been completed");
        response.setTask(updatedTask);

        return response;
    }
}
