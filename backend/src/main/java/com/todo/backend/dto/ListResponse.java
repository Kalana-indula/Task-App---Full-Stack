package com.todo.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ListResponse<T> {

    private String message;
    private List<T> entityList=new ArrayList<>();
}
