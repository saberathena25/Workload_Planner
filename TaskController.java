package com.example.workload_planner.controller;

import com.example.workload_planner.model.Task;
import com.example.workload_planner.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {
    
    private final TaskService taskService;
    
    @PostMapping
    public ResponseEntity<Task> addTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.addTask(task);
        return ResponseEntity.ok(createdTask);
    }
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    
    @DeleteMapping
    public ResponseEntity<Void> clearAllTasks() {
        taskService.clearAllTasks();
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/workload")
    public ResponseEntity<Map<String, Object>> getWorkloadInfo() {
        Map<String, Object> workloadInfo = new HashMap<>();
        workloadInfo.put("totalMinutes", taskService.getTotalWorkloadMinutes());
        workloadInfo.put("stressScore", taskService.calculateStressScore());
        workloadInfo.put("tasks", taskService.getAllTasks());
        
        return ResponseEntity.ok(workloadInfo);
    }
}