package com.umwa.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import com.umwa.data.MiningSession;
import java.util.*;

public class PerformanceAnalyzer {
    private final Map<ChunkPos, Long> chunkMiningTimes = new HashMap<>();
    private final Map<Integer, Integer> blocksMinedByHour = new HashMap<>();
    private final List<MiningSession> historicalSessions = new ArrayList<>();
    private long sessionStartTime;
    private int totalMouseClicks = 0;
    private double averageEfficiency = 0.0;
    
    public PerformanceAnalyzer() {
        this.sessionStartTime = System.currentTimeMillis();
    }
    
    public void recordChunkMiningTime(ChunkPos chunk, long timeSpent) {
        chunkMiningTimes.put(chunk, timeSpent);
    }
    
    public void recordHourlyMining() {
        int currentHour = (int) ((System.currentTimeMillis() - sessionStartTime) / 3600000);
        blocksMinedByHour.merge(currentHour, 1, Integer::sum);
    }
    
    public MiningEfficiencyReport generateEfficiencyReport() {
        MiningEfficiencyReport report = new MiningEfficiencyReport();
        
        // Calculate mining speed trends
        List<Integer> hourlyRates = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            hourlyRates.add(blocksMinedByHour.getOrDefault(hour, 0));
        }
        report.hourlyMiningRates = hourlyRates;
        
        // Calculate best performing chunks
        report.bestChunks = chunkMiningTimes.entrySet()
            .stream()
            .sorted(Map.Entry.<ChunkPos, Long>comparingByValue())
            .limit(10)
            .map(Map.Entry::getKey)
            .toList();
        
        // Performance score (0-100)
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        int totalBlocks = blocksMinedByHour.values().stream().mapToInt(Integer::intValue).sum();
        
        if (sessionDuration > 0) {
            double blocksPerMinute = (double) totalBlocks / (sessionDuration / 60000.0);
            report.performanceScore = Math.min(100, (int) (blocksPerMinute * 2)); // 50 blocks/min = 100 score
        }
        
        return report;
    }
    
    public List<String> generateOptimizationSuggestions() {
        List<String> suggestions = new ArrayList<>();
        
        // Analyze mining patterns
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        int totalBlocks = blocksMinedByHour.values().stream().mapToInt(Integer::intValue).sum();
        
        if (sessionDuration > 300000 && totalBlocks > 0) { // 5+ minutes
            double blocksPerMinute = (double) totalBlocks / (sessionDuration / 60000.0);
            
            if (blocksPerMinute < 20) {
                suggestions.add("Consider using Efficiency enchantments on your pickaxe");
                suggestions.add("Try branch mining for better ore/block ratio");
            }
            
            if (averageEfficiency < 0.05) {
                suggestions.add("Focus mining at Y-level -11 to -16 for optimal diamond yields");
                suggestions.add("Use the minimap to identify unexplored cave systems");
            }
            
            if (chunkMiningTimes.size() > 10) {
                // Find chunks with unusually long mining times
                double avgTime = chunkMiningTimes.values().stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);
                
                long slowChunks = chunkMiningTimes.values().stream()
                    .mapToLong(Long::longValue)
                    .filter(time -> time > avgTime * 2)
                    .count();
                
                if (slowChunks > 3) {
                    suggestions.add("Some areas are taking unusually long to mine - check for lava/water obstacles");
                    suggestions.add("Consider using the AI path finder for more efficient routes");
                }
            }
        }
        
        // Time-based suggestions
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        
        if (hour >= 2 && hour <= 6) {
            suggestions.add("Late night mining detected - consider taking breaks to maintain efficiency");
        }
        
        return suggestions;
    }
    
    public void recordMouseClick() {
        totalMouseClicks++;
    }
    
    public void updateEfficiency(double efficiency) {
        this.averageEfficiency = (this.averageEfficiency + efficiency) / 2.0;
    }
    
    public int getClicksPerMinute() {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        if (sessionDuration > 0) {
            return (int) ((double) totalMouseClicks / (sessionDuration / 60000.0));
        }
        return 0;
    }
    
    public Map<String, Object> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        int totalBlocks = blocksMinedByHour.values().stream().mapToInt(Integer::intValue).sum();
        
        metrics.put("sessionDuration", sessionDuration);
        metrics.put("totalBlocks", totalBlocks);
        metrics.put("blocksPerMinute", sessionDuration > 0 ? 
            (double) totalBlocks / (sessionDuration / 60000.0) : 0.0);
        metrics.put("clicksPerMinute", getClicksPerMinute());
        metrics.put("averageEfficiency", averageEfficiency);
        metrics.put("chunksExplored", chunkMiningTimes.size());
        
        return metrics;
    }
    
    public static class MiningEfficiencyReport {
        public List<Integer> hourlyMiningRates;
        public List<ChunkPos> bestChunks;
        public int performanceScore;
        public double averageEfficiency;
        public String grade;
        
        public String getGrade() {
            if (performanceScore >= 90) return "A+";
            if (performanceScore >= 80) return "A";
            if (performanceScore >= 70) return "B+";
            if (performanceScore >= 60) return "B";
            if (performanceScore >= 50) return "C+";
            if (performanceScore >= 40) return "C";
            return "D";
        }
    }
}