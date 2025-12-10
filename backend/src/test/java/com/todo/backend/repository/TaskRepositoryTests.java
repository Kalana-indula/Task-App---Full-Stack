package com.todo.backend.repository;

import com.todo.backend.entity.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TaskRepositoryTests {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void TaskRepository_SavedTaskNotNull(){

        //arrange
        Task task = Task.builder()
                .taskId("TSK-TEST-1")
                .title("Test Task")
                .description("Testing repository save")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        //act
        Task savedTask = taskRepository.save(task);

        //assert
        Assertions.assertNotNull(savedTask);
        Assertions.assertNotNull(savedTask.getId());
    }

    @Test
    public void TaskRepository_FindRecentTasks_ReturnsLastFiveIncompleteTasks() {

        // arrange
        for (int i = 1; i <= 10; i++) {
            Task task = Task.builder()
                    .taskId("TSK " + i)
                    .title("Task " + i)
                    .description("Test " + i)
                    .completed(i % 2 == 0)
                    .createdAt(LocalDateTime.now())
                    .build();

            taskRepository.save(task);
        }

        // act
        List<Task> recentTasks = taskRepository.findRecentTasks();

        // assert - get 5 values
        Assertions.assertEquals(5, recentTasks.size());

        // assert - check whether returned tasks have completed
        Assertions.assertTrue(recentTasks.stream().allMatch(t -> !t.getCompleted()));

        // assert - if tasks in descending order
        for (int i = 0; i < recentTasks.size() - 1; i++) {
            Assertions.assertTrue(recentTasks.get(i).getId() > recentTasks.get(i + 1).getId());
        }
    }

}
