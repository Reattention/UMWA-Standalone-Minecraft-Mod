package com.umwa.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import java.util.HashMap;
import java.util.Map;

public class HotSpot {
    private final BlockPos center;
    private final Map<Block, Integer> oreDistribution;
    private int totalOres;
    private int valueScore;
    private double density;
    private final long discoveredTime;
    
    public HotSpot(BlockPos center, Block initialOre) {
        this.center = center;
        this.oreDistribution = new HashMap<>();
        this.oreDistribution.put(initialOre, 1);
        this.totalOres = 1;
        this.valueScore = calculateOreValue(initialOre);
        this.density = 1.0;
        this.discoveredTime = System.currentTimeMillis();
    }
    
    public void addOre(BlockPos orePos, Block oreType) {
        oreDistribution.merge(oreType, 1, Integer::sum);
        totalOres++;
        valueScore += calculateOreValue(oreType);
        updateDensity();
    }
    
    private int calculateOreValue(Block oreType) {
        String oreName = oreType.toString().toLowerCase();
        
        if (oreName.contains("diamond")) return 100;
        if (oreName.contains("emerald")) return 80;
        if (oreName.contains("ancient_debris")) return 120;
        if (oreName.contains("gold")) return 40;
        if (oreName.contains("iron")) return 20;
        if (oreName.contains("copper")) return 10;
        if (oreName.contains("coal")) return 5;
        if (oreName.contains("redstone")) return 15;
        if (oreName.contains("lapis")) return 15;
        
        return 1; // Default value for unknown ores
    }
    
    private void updateDensity() {
        // Calculate ore density in a 16x16x16 area
        int volume = 16 * 16 * 16;
        this.density = (double) totalOres / volume;
    }
    
    public BlockPos getCenter() {
        return center;
    }
    
    public Map<Block, Integer> getOreDistribution() {
        return new HashMap<>(oreDistribution);
    }
    
    public int getTotalOres() {
        return totalOres;
    }
    
    public int getValueScore() {
        return valueScore;
    }
    
    public double getDensity() {
        return density;
    }
    
    public long getDiscoveredTime() {
        return discoveredTime;
    }
    
    public String getMostValuableOre() {
        Block mostValuable = null;
        int highestValue = 0;
        
        for (Block ore : oreDistribution.keySet()) {
            int value = calculateOreValue(ore);
            if (value > highestValue) {
                highestValue = value;
                mostValuable = ore;
            }
        }
        
        return mostValuable != null ? mostValuable.toString() : "Unknown";
    }
    
    public double getDistanceFrom(BlockPos pos) {
        return Math.sqrt(center.getSquaredDistance(pos));
    }
    
    public boolean isWithinRadius(BlockPos pos, int radius) {
        return center.isWithinDistance(pos, radius);
    }
    
    public String getHotspotSummary() {
        return String.format("HotSpot at %s: %d ores, Value: %d, Density: %.3f", 
                center.toShortString(), totalOres, valueScore, density);
    }
}