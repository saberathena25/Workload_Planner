package com.example.workload_planner.service;

import com.example.workload_planner.model.StressData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StressDataService {
    
    private final List<StressData> stressDataList = new CopyOnWriteArrayList<>();
    
    @PostConstruct
    public void loadStressData() {
        try {
            ClassPathResource resource = new ClassPathResource("Stress.csv");
            try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
                List<String[]> records = reader.readAll();
                
                // Skip header row
                for (int i = 1; i < records.size(); i++) {
                    String[] record = records.get(i);
                    if (record.length >= 7) {
                        try {
                            StressData data = new StressData();
                            data.setSubreddit(record[0]);
                            data.setPostId(record[1]);
                            data.setSentenceRange(record[2]);
                            data.setText(record[3]);
                            data.setLabel(record[4].isEmpty() ? null : Integer.parseInt(record[4]));
                            data.setConfidence(record[5].isEmpty() ? null : Double.parseDouble(record[5]));
                            data.setSocialTimestamp(record[6]);
                            stressDataList.add(data);
                        } catch (Exception e) {
                            log.warn("Error parsing row {}: {}", i, e.getMessage());
                        }
                    }
                }
                log.info("Loaded {} stress data records", stressDataList.size());
            }
        } catch (IOException | CsvException e) {
            log.error("Error loading stress data: {}", e.getMessage());
        }
    }
    
    public List<StressData> getAllStressData() {
        return new ArrayList<>(stressDataList);
    }
    
    public Map<String, Object> getStressStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalRecords = stressDataList.size();
        long stressedCount = stressDataList.stream()
                .filter(StressData::isStressed)
                .count();
        long notStressedCount = totalRecords - stressedCount;
        
        double stressPercentage = totalRecords > 0 ? (stressedCount * 100.0 / totalRecords) : 0;
        
        stats.put("totalRecords", totalRecords);
        stats.put("stressedCount", stressedCount);
        stats.put("notStressedCount", notStressedCount);
        stats.put("stressPercentage", Math.round(stressPercentage * 100.0) / 100.0);
        
        // Average confidence
        double avgConfidence = stressDataList.stream()
                .filter(d -> d.getConfidence() != null)
                .mapToDouble(StressData::getConfidence)
                .average()
                .orElse(0.0);
        stats.put("averageConfidence", Math.round(avgConfidence * 100.0) / 100.0);
        
        return stats;
    }
    
    public Map<String, Long> getStressBySubreddit() {
        return stressDataList.stream()
                .filter(StressData::isStressed)
                .collect(Collectors.groupingBy(
                        StressData::getSubreddit,
                        Collectors.counting()
                ));
    }
    
    public List<StressData> getStressedPosts(int limit) {
        return stressDataList.stream()
                .filter(StressData::isStressed)
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    public List<StressData> searchByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return stressDataList.stream()
                .filter(d -> d.getText() != null && d.getText().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
    
    public Map<String, Object> getConfidenceDistribution() {
        Map<String, Object> distribution = new HashMap<>();
        
        long highConfidence = stressDataList.stream()
                .filter(d -> d.getConfidence() != null && d.getConfidence() >= 0.8)
                .count();
        
        long mediumConfidence = stressDataList.stream()
                .filter(d -> d.getConfidence() != null && d.getConfidence() >= 0.5 && d.getConfidence() < 0.8)
                .count();
        
        long lowConfidence = stressDataList.stream()
                .filter(d -> d.getConfidence() != null && d.getConfidence() < 0.5)
                .count();
        
        distribution.put("high", highConfidence);
        distribution.put("medium", mediumConfidence);
        distribution.put("low", lowConfidence);
        
        return distribution;
    }
}
