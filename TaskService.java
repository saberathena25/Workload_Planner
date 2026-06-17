package com.example.workload_planner.service;

import com.example.workload_planner.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TaskService {
    
    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    
    public Task addTask(Task task) {
        tasks.add(task);
        return task;
       
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    public void clearAllTasks() {
        tasks.clear();
    }
    
    public int getTotalWorkloadMinutes() {
        return tasks.stream()
                .mapToInt(Task::getDuration)
                .sum();
    }
    
    public double calculateStressScore() {
        // Simple stress calculation based on workload and priorities
        int totalMinutes = getTotalWorkloadMinutes();
        
        // Base stress is proportional to total minutes
        double baseStress = totalMinutes / 60.0; // Hours
        
        // Adjust based on priority distribution
        long highPriorityCount = tasks.stream()
                .filter(task -> task.getPriority() == Task.Priority.HIGH)
                .count();
        
        long mediumPriorityCount = tasks.stream()
                .filter(task -> task.getPriority() == Task.Priority.MEDIUM)
                .count();
        
        // Higher stress for more high-priority tasks
        double priorityAdjustment = (highPriorityCount * 1.5) + (mediumPriorityCount * 0.75);
        
        // Combine factors
        double stressScore = baseStress + priorityAdjustment;
        
        // Normalize to a 0-10 scale
        return Math.min(10.0, stressScore / 5.0);
    }
}
