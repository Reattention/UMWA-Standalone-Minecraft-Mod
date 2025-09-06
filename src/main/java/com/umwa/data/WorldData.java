package com.umwa.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class WorldData {
    private final String worldName;
    private final Map<String, Object> analysisData;
    private final Set<String> exploredChunks;
    private long totalMiningTime;
    private int totalBlocksMined;
    private boolean isAnalyzed;
    
    public WorldData(String worldName) {
        this.worldName = worldName;
        this.analysisData = new HashMap<>();
        this.exploredChunks = new HashSet<>();
        this.totalMiningTime = 0;
        this.totalBlocksMined = 0;
        this.isAnalyzed = false;
    }
    
    public void setAnalysisData(String key, Object value) {
        analysisData.put(key, value);
    }
    
    public Object getAnalysisData(String key) {
        return analysisData.get(key);
    }
    
    public void markChunkExplored(String chunkKey) {
        exploredChunks.add(chunkKey);
    }
    
    public boolean isChunkExplored(String chunkKey) {
        return exploredChunks.contains(chunkKey);
    }
    
    public String getWorldName() {
        return worldName;
    }
    
    public Set<String> getExploredChunks() {
        return new HashSet<>(exploredChunks);
    }
    
    public long getTotalMiningTime() {
        return totalMiningTime;
    }
    
    public void addMiningTime(long time) {
        this.totalMiningTime += time;
    }
    
    public int getTotalBlocksMined() {
        return totalBlocksMined;
    }
    
    public void addBlocksMined(int blocks) {
        this.totalBlocksMined += blocks;
    }
    
    public boolean isAnalyzed() {
        return isAnalyzed;
    }
    
    public void setAnalyzed(boolean analyzed) {
        this.isAnalyzed = analyzed;
    }
    
    public double getExplorationPercentage() {
        // Rough estimate based on explored chunks
        // This would need to be more sophisticated in a real implementation
        return Math.min(100.0, exploredChunks.size() * 0.1);
    }
}