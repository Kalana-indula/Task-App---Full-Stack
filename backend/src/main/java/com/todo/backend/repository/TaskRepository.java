package com.todo.backend.repository;

import com.todo.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // find last saved task id
    Optional<Task> findTopByOrderByIdDesc();

    //find recent tasks
    @Query(
            value = "SELECT * FROM task WHERE completed = false ORDER BY id DESC LIMIT 5",
            nativeQuery = true
    )
    List<Task> findRecentTasks();

}
