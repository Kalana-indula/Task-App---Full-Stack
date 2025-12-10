package com.todo.backend.controller;

import com.todo.backend.dto.AddTaskDto;
import com.todo.backend.entity.Task;
import com.todo.backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // clean DB before each test
        taskRepository.deleteAll();
    }

    @Test
    void createTask_ValidRequest_PersistsAndReturnsTask() throws Exception {
        // arrange
        AddTaskDto dto = new AddTaskDto();
        dto.setTitle("Integration Task");
        dto.setDescription("Integration Description");

        // act + assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.taskId").exists())
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andExpect(jsonPath("$.description").value("Integration Description"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void findRecentTasks_ReturnsTasksFromDatabase() throws Exception {
        // arrange
        Task t1 = Task.builder()
                .taskId("TSK 1")
                .title("Task 1")
                .description("Desc 1")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        Task t2 = Task.builder()
                .taskId("TSK 2")
                .title("Task 2")
                .description("Desc 2")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        taskRepository.save(t1); // smaller id
        taskRepository.save(t2); // larger id

        // act + assert
        mockMvc.perform(get("/api/tasks/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No of tasks found : 2"))
                .andExpect(jsonPath("$.entityList.length()").value(2))
                .andExpect(jsonPath("$.entityList[0].taskId").value("TSK 2"))
                .andExpect(jsonPath("$.entityList[1].taskId").value("TSK 1"));
    }

    @Test
    void completeTask_ValidId_CompletesTask() throws Exception {
        // arrange
        Task task = Task.builder()
                .taskId("TSK 1")
                .title("To Complete")
                .description("Desc")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        Task saved = taskRepository.save(task);

        // act + assert
        mockMvc.perform(put("/api/tasks/{id}/complete", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task has been completed"))
                .andExpect(jsonPath("$.task.id").value(saved.getId()))
                .andExpect(jsonPath("$.task.completed").value(true));
    }

    @Test
    void completeTask_InvalidId_ReturnsNotFoundError() throws Exception {
        // act + assert
        mockMvc.perform(put("/api/tasks/{id}/complete", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task 9999 not found"));
    }

    @Test
    void createTask_EmptyTitle_ReturnsBadRequest() throws Exception {
        // arrange
        AddTaskDto dto = new AddTaskDto();
        dto.setTitle(""); // invalid, empty
        dto.setDescription("Some description");

        // act + assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Title cannot be empty")));
    }

    @Test
    void createTask_EmptyDescription_ReturnsBadRequest() throws Exception {
        // arrange
        AddTaskDto dto = new AddTaskDto();
        dto.setTitle("Valid title");
        dto.setDescription(""); // invalid, empty

        // act + assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Description cannot be empty")));
    }
}
