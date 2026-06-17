package com.example.workload_planner.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Task {
    @NotBlank(message = "Task name is required")
    private String name;
    
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer duration; // in minutes
    
    @NotNull(message = "Priority is required")
    private Priority priority;
    
    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}