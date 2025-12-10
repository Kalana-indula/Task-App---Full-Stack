package com.todo.backend.service;


import com.todo.backend.dto.AddTaskDto;
import com.todo.backend.dto.CompleteResponse;
import com.todo.backend.dto.ListResponse;
import com.todo.backend.entity.Task;
import com.todo.backend.exception.TaskNotFoundException;
import com.todo.backend.repository.TaskRepository;
import com.todo.backend.service.impl.TaskServiceImpl;
import com.todo.backend.util.TaskIdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskIdGenerator taskIdGenerator;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    public void TaskService_CreateTask_ReturnsTask() {

        // arrange
        AddTaskDto addTaskDto = new AddTaskDto();
        addTaskDto.setTitle("Test Task");
        addTaskDto.setDescription("Test Description");

        // mock ID generator
        Mockito.when(taskIdGenerator.generate())
                .thenReturn("TSK 2");

        Task savedTask = Task.builder()
                .id(2L)
                .taskId("TSK 2")
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenReturn(savedTask);

        // act
        Task result = taskService.createTask(addTaskDto);

        // assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Task", result.getTitle());
        Assertions.assertEquals("Test Description", result.getDescription());
        Assertions.assertEquals("TSK 2", result.getTaskId());
        Assertions.assertFalse(result.getCompleted());

        Mockito.verify(taskIdGenerator, Mockito.times(1)).generate();  // optional check
        Mockito.verify(taskRepository, Mockito.times(1))
                .save(Mockito.any(Task.class));
    }

    //test whether returns a ListResponse as required
    @Test
    public void TaskService_FindRecentTasks_ReturnsListResponse() {
        // arrange
        Task task1 = Task.builder()
                .id(1L)
                .title("Task 1")
                .description("Desc 1")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("Task 2")
                .description("Desc 2")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        List<Task> tasks = Arrays.asList(task1, task2);

        Mockito.when(taskRepository.findRecentTasks())
                .thenReturn(tasks);

        // act
        ListResponse<Task> response = taskService.findRecentTasks();

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("No of tasks found : 2", response.getMessage());
        Assertions.assertEquals(2, response.getEntityList().size());
        Assertions.assertEquals(tasks, response.getEntityList());
        Mockito.verify(taskRepository, Mockito.times(1)).findRecentTasks();
    }

    @Test
    public void TaskService_FindRecentTasks_NoTasks_ReturnsMessageOnly() {
        // arrange
        Mockito.when(taskRepository.findRecentTasks())
                .thenReturn(Collections.emptyList());

        // act
        ListResponse<Task> response = taskService.findRecentTasks();

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("No tasks found", response.getMessage());
        Assertions.assertTrue(response.getEntityList().isEmpty());
        Mockito.verify(taskRepository, Mockito.times(1)).findRecentTasks();
    }

    @Test
    public void TaskService_CompleteTask_ExistingId_ReturnsCompleteResponse() {
        // Arrange
        Long id = 1L;

        Task existingTask = Task.builder()
                .id(id)
                .title("Incomplete Task")
                .description("Desc")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        Task updatedTask = Task.builder()
                .id(id)
                .title("Incomplete Task")
                .description("Desc")
                .completed(true)
                .createdAt(existingTask.getCreatedAt())
                .build();

        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(existingTask));

        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenReturn(updatedTask);

        // act
        CompleteResponse response = taskService.completeTask(id);

        // assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Task has been completed", response.getMessage());
        Assertions.assertNotNull(response.getTask());
        Assertions.assertTrue(response.getTask().getCompleted());
        Assertions.assertEquals(id, response.getTask().getId());

        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
    }

    @Test
    public void TaskService_CompleteTask_TaskNotFound_ThrowsException() {
        // arrange
        Long id = 99L;

        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        //act
        Exception exception = Assertions.assertThrows(
                TaskNotFoundException.class,
                () -> taskService.completeTask(id)
        );

        // assert
        Assertions.assertEquals("Task 99 not found", exception.getMessage());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
        Mockito.verify(taskRepository, Mockito.never()).save(Mockito.any(Task.class));
    }

}
