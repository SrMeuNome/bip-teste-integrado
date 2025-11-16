package com.example.backend.presentation.rest.v1.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private T data;
    private List<String> errors;
    private Map<String, String> fieldErrors;
    private String message;
}
