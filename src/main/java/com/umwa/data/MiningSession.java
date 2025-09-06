package com.umwa.data;

import net.minecraft.block.Block;
import java.util.HashMap;
import java.util.Map;

public class MiningSession {
    private int blocksMined = 0;
    private final Map<Block, Integer> oresFound = new HashMap<>();
    private long sessionStartTime;
    private double efficiency = 0.0;
    
    public MiningSession() {
        this.sessionStartTime = System.currentTimeMillis();
    }
    
    public void incrementBlocksMined() {
        blocksMined++;
    }
    
    public void addOreFound(Block oreType) {
        oresFound.merge(oreType, 1, Integer::sum);
    }
    
    public void updateEfficiency() {
        int totalOres = oresFound.values().stream().mapToInt(Integer::intValue).sum();
        this.efficiency = blocksMined > 0 ? (double) totalOres / blocksMined : 0.0;
    }
    
    public int getBlocksMined() {
        return blocksMined;
    }
    
    public Map<Block, Integer> getOresFound() {
        return new HashMap<>(oresFound);
    }
    
    public int getTotalOres() {
        return oresFound.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public double getEfficiency() {
        return efficiency;
    }
    
    public long getSessionDuration() {
        return System.currentTimeMillis() - sessionStartTime;
    }
    
    public double getBlocksPerMinute() {
        long durationMs = getSessionDuration();
        if (durationMs == 0) return 0;
        return (double) blocksMined / (durationMs / 60000.0);
    }
}