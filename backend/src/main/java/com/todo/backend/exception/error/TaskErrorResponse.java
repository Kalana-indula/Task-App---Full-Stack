package com.todo.backend.exception.error;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskErrorResponse {
    private Integer status;
    private String message;
    private Long timeStamp;
}
