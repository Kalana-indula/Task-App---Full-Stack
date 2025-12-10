package com.todo.backend.service;

import com.todo.backend.dto.AddTaskDto;
import com.todo.backend.dto.CompleteResponse;
import com.todo.backend.dto.ListResponse;
import com.todo.backend.entity.Task;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {

    Task createTask(AddTaskDto addTaskDto);

    ListResponse<Task> findRecentTasks();

    CompleteResponse completeTask(Long id);
}
