package com.todo.backend.service;

import com.todo.backend.dto.AddTaskDto;
import com.todo.backend.dto.CompleteResponse;
import com.todo.backend.dto.ListResponse;
import com.todo.backend.entity.Task;
import com.todo.backend.exception.TaskNotFoundException;
import com.todo.backend.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TaskServiceIntegrationTests {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDb() {
        taskRepository.deleteAll();
    }


    @Test
    void createTask_PersistsToDatabase() {
        // arrange
        AddTaskDto dto = new AddTaskDto();
        dto.setTitle("Service Task");
        dto.setDescription("Service Desc");

        // act
        Task created = taskService.createTask(dto);

        // assert
        Assertions.assertNotNull(created.getId());
        Assertions.assertNotNull(created.getTaskId());
        Assertions.assertEquals("Service Task", created.getTitle());

        Task fromDb = taskRepository.findById(created.getId()).orElseThrow();
        Assertions.assertEquals("Service Task", fromDb.getTitle());
    }

    @Test
    void findRecentTasks_ReturnsListResponse() {
        // Arrange
        for (int i = 1; i <= 3; i++) {
            Task t = Task.builder()
                    .taskId("TSK " + i)
                    .title("Task " + i)
                    .description("Desc " + i)
                    .completed(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            taskRepository.save(t);
        }

        // act
        ListResponse<Task> response = taskService.findRecentTasks();

        // assert
        Assertions.assertEquals("No of tasks found : 3", response.getMessage());
        Assertions.assertEquals(3, response.getEntityList().size());
    }

    @Test
    void completeTask_ExistingTask_UpdatesCompletion() {
        // arrange
        Task t = Task.builder()
                .taskId("TSK 1")
                .title("To Complete")
                .description("Desc")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();
        Task saved = taskRepository.save(t);

        // act
        CompleteResponse response = taskService.completeTask(saved.getId());

        //assert
        Assertions.assertEquals("Task has been completed", response.getMessage());
        Assertions.assertTrue(response.getTask().getCompleted());

        Task fromDb = taskRepository.findById(saved.getId()).orElseThrow();
        Assertions.assertTrue(fromDb.getCompleted());
    }

    @Test
    void completeTask_TaskNotFound_ThrowsException() {
        // act + assert
        TaskNotFoundException ex = Assertions.assertThrows(
                TaskNotFoundException.class,
                () -> taskService.completeTask(999L)
        );

        Assertions.assertEquals("Task 999 not found", ex.getMessage());
    }
}
