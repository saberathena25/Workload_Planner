package com.example.workload_planner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StressData {
    private String subreddit;
    private String postId;
    private String sentenceRange;
    private String text;
    private Integer label; // 0 = no stress, 1 = stress
    private Double confidence;
    private String socialTimestamp;
    
    public boolean isStressed() {
        return label != null && label == 1;
    }
}
