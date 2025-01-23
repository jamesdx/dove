package com.helix.dove.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MetricsDTO {
    private Map<String, Double> counters = new HashMap<>();
    private Map<String, Double> gauges = new HashMap<>();
    private Map<String, Timer> timers = new HashMap<>();

    @Data
    public static class Timer {
        private double count;
        private double totalTime;
        private double max;
        private double mean;
        private Map<Double, Double> percentiles = new HashMap<>();
    }
}