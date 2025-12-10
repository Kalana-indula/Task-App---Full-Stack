package com.todo.backend.dto;

import com.todo.backend.entity.Task;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompleteResponse {

    private String message;

    private Task task;
}
