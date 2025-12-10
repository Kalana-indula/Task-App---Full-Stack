package com.todo.backend.controller;

import com.todo.backend.dto.AddTaskDto;
import com.todo.backend.dto.ListResponse;
import com.todo.backend.entity.Task;
import com.todo.backend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    private Task sampleTask;
    private AddTaskDto addTaskDto;

    @BeforeEach
    void setUp() {
        sampleTask = Task.builder()
                .id(1L)
                .taskId("TSK 1")
                .title("Sample Task")
                .description("Sample Description")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        addTaskDto = new AddTaskDto();
        addTaskDto.setTitle(sampleTask.getTitle());
        addTaskDto.setDescription(sampleTask.getDescription());
    }

    @Test
    void createTask_ReturnsCreatedTask() throws Exception {
        Mockito.when(taskService.createTask(Mockito.any(AddTaskDto.class)))
                .thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addTaskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleTask.getId()))
                .andExpect(jsonPath("$.taskId").value(sampleTask.getTaskId()))
                .andExpect(jsonPath("$.title").value(sampleTask.getTitle()))
                .andExpect(jsonPath("$.description").value(sampleTask.getDescription()))
                .andExpect(jsonPath("$.completed").value(sampleTask.getCompleted()));
    }

    @Test
    void findRecentTasks_ReturnsListResponse() throws Exception {
        List<Task> tasks = Arrays.asList(
                sampleTask,
                sampleTask.toBuilder().id(2L).taskId("TSK 2").build()
        );

        ListResponse<Task> response = new ListResponse<>();
        response.setMessage("No of tasks found : " + tasks.size());
        response.setEntityList(tasks);

        Mockito.when(taskService.findRecentTasks()).thenReturn(response);

        mockMvc.perform(get("/api/tasks/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No of tasks found : 2"))
                .andExpect(jsonPath("$.entityList.length()").value(2))
                .andExpect(jsonPath("$.entityList[0].taskId").value("TSK 1"))
                .andExpect(jsonPath("$.entityList[1].taskId").value("TSK 2"));
    }

    @Test
    void createTask_EmptyTitle_ReturnsBadRequest() throws Exception {
        AddTaskDto dto = new AddTaskDto();
        dto.setTitle("");// invalid
        dto.setDescription("Valid description");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Title cannot be empty")));
    }

    @Test
    void createTask_EmptyDescription_ReturnsBadRequest() throws Exception {
        AddTaskDto dto = new AddTaskDto();
        dto.setTitle("Valid title");
        dto.setDescription(""); // invalid

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Description cannot be empty")));
    }

    @Test
    void createTask_MissingFields_ReturnsBadRequest() throws Exception {
        AddTaskDto dto = new AddTaskDto(); // both fields null, invalid

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                // message may return first validation error (implementation dependent)
                .andExpect(jsonPath("$.message", containsString("cannot be empty")));
    }
}
