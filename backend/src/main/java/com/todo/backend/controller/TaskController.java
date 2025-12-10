package com.todo.backend.controller;

import com.todo.backend.dto.AddTaskDto;
import com.todo.backend.dto.CompleteResponse;
import com.todo.backend.dto.ListResponse;
import com.todo.backend.entity.Task;
import com.todo.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    // constructor injection
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // create a new task
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody AddTaskDto addTaskDto) {
        Task createdTask = taskService.createTask(addTaskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }


    // find recent tasks
    @GetMapping("/recent")
    public ResponseEntity<ListResponse<Task>> findRecentTasks() {
        ListResponse<Task> response = taskService.findRecentTasks();
        return ResponseEntity.ok(response);
    }

    // complete task
    @PutMapping("/{id}/complete")
    public ResponseEntity<CompleteResponse> completeTask(@PathVariable Long id) {
        CompleteResponse response = taskService.completeTask(id);
        return ResponseEntity.ok(response);
    }
}
