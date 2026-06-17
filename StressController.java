package com.example.workload_planner.controller;

import com.example.workload_planner.model.StressData;
import com.example.workload_planner.service.StressDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stress")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StressController {
    
    private final StressDataService stressDataService;
    
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(stressDataService.getStressStatistics());
    }
    
    @GetMapping("/by-subreddit")
    public ResponseEntity<Map<String, Long>> getStressBySubreddit() {
        return ResponseEntity.ok(stressDataService.getStressBySubreddit());
    }
    
    @GetMapping("/stressed-posts")
    public ResponseEntity<List<StressData>> getStressedPosts(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(stressDataService.getStressedPosts(limit));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<StressData>> searchByKeyword(
            @RequestParam String keyword) {
        return ResponseEntity.ok(stressDataService.searchByKeyword(keyword));
    }
    
    @GetMapping("/confidence-distribution")
    public ResponseEntity<Map<String, Object>> getConfidenceDistribution() {
        return ResponseEntity.ok(stressDataService.getConfidenceDistribution());
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<StressData>> getAllData(
            @RequestParam(defaultValue = "100") int limit) {
        List<StressData> allData = stressDataService.getAllStressData();
        return ResponseEntity.ok(allData.subList(0, Math.min(limit, allData.size())));
    }
}
