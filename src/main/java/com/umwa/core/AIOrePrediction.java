package com.umwa.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import com.umwa.data.HotSpot;
import java.util.*;

public class AIOrePrediction {
    private final Map<Block, Double> oreFrequencies = new HashMap<>();
    private final Map<Integer, Double> yLevelDistribution = new HashMap<>();
    private final List<BlockPos> knownOrePositions = new ArrayList<>();
    
    public AIOrePrediction() {
        initializeOreFrequencies();
        initializeYLevelDistribution();
    }
    
    private void initializeOreFrequencies() {
        // Initialize based on Minecraft ore generation patterns
        oreFrequencies.put(Blocks.DIAMOND_ORE, 0.0012);
        oreFrequencies.put(Blocks.DEEPSLATE_DIAMOND_ORE, 0.0015);
        oreFrequencies.put(Blocks.EMERALD_ORE, 0.0008);
        oreFrequencies.put(Blocks.GOLD_ORE, 0.0025);
        oreFrequencies.put(Blocks.IRON_ORE, 0.0100);
        oreFrequencies.put(Blocks.COPPER_ORE, 0.0080);
        oreFrequencies.put(Blocks.COAL_ORE, 0.0200);
        oreFrequencies.put(Blocks.REDSTONE_ORE, 0.0060);
        oreFrequencies.put(Blocks.LAPIS_ORE, 0.0035);
        oreFrequencies.put(Blocks.ANCIENT_DEBRIS, 0.0003);
    }
    
    private void initializeYLevelDistribution() {
        // Diamond distribution peaks
        for (int y = -64; y <= 16; y++) {
            if (y >= -16 && y <= -8) {
                yLevelDistribution.put(y, 1.0); // Peak diamond level
            } else if (y >= -32 && y <= 0) {
                yLevelDistribution.put(y, 0.7); // Good diamond level
            } else if (y >= -64 && y <= 16) {
                yLevelDistribution.put(y, 0.3); // Possible diamond level
            } else {
                yLevelDistribution.put(y, 0.0);
            }
        }
    }
    
    public double predictOreChance(BlockPos pos, Block oreType) {
        double baseChance = oreFrequencies.getOrDefault(oreType, 0.001);
        double yLevelModifier = yLevelDistribution.getOrDefault(pos.getY(), 0.1);
        double proximityBonus = calculateProximityBonus(pos, oreType);
        double biomeModifier = getBiomeModifier(pos, oreType);
        
        return Math.min(1.0, baseChance * yLevelModifier * proximityBonus * biomeModifier);
    }
    
    private double calculateProximityBonus(BlockPos pos, Block oreType) {
        double bonus = 1.0;
        
        for (BlockPos orePos : knownOrePositions) {
            double distance = Math.sqrt(pos.getSquaredDistance(orePos));
            if (distance < 16) {
                // Ore veins tend to cluster
                bonus += (16 - distance) / 16 * 0.5;
            }
        }
        
        return Math.min(bonus, 3.0); // Cap the bonus
    }
    
    private double getBiomeModifier(BlockPos pos, Block oreType) {
        // Simplified biome-based modifiers
        if (oreType == Blocks.EMERALD_ORE) {
            // Emeralds only in mountain biomes (simplified check by Y level)
            return pos.getY() > 100 ? 2.0 : 0.1;
        }
        
        return 1.0; // Default modifier
    }
    
    public void addKnownOre(BlockPos pos, Block oreType) {
        knownOrePositions.add(pos);
        
        // Update frequency learning
        double currentFreq = oreFrequencies.getOrDefault(oreType, 0.001);
        oreFrequencies.put(oreType, currentFreq * 1.01); // Slight increase
    }
    
    public List<BlockPos> predictNextBestMiningSpots(BlockPos playerPos, int radius, int count) {
        List<BlockPos> candidates = new ArrayList<>();
        
        // Generate candidates in a sphere around the player
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x*x + y*y + z*z <= radius*radius) {
                        candidates.add(playerPos.add(x, y, z));
                    }
                }
            }
        }
        
        // Score candidates based on diamond prediction
        candidates.sort((a, b) -> {
            double scoreA = predictOreChance(a, Blocks.DIAMOND_ORE) + 
                           predictOreChance(a, Blocks.DEEPSLATE_DIAMOND_ORE);
            double scoreB = predictOreChance(b, Blocks.DIAMOND_ORE) + 
                           predictOreChance(b, Blocks.DEEPSLATE_DIAMOND_ORE);
            return Double.compare(scoreB, scoreA);
        });
        
        return candidates.subList(0, Math.min(count, candidates.size()));
    }
    
    public Map<Block, Double> getOreProbabilitiesAtPosition(BlockPos pos) {
        Map<Block, Double> probabilities = new HashMap<>();
        
        for (Block oreType : oreFrequencies.keySet()) {
            probabilities.put(oreType, predictOreChance(pos, oreType));
        }
        
        return probabilities;
    }
    
    public String generateMiningReport(BlockPos playerPos) {
        StringBuilder report = new StringBuilder();
        report.append("AI Mining Analysis Report\n");
        report.append("========================\n");
        report.append("Position: ").append(playerPos.toShortString()).append("\n\n");
        
        Map<Block, Double> probs = getOreProbabilitiesAtPosition(playerPos);
        report.append("Ore Predictions at current position:\n");
        for (Map.Entry<Block, Double> entry : probs.entrySet()) {
            if (entry.getValue() > 0.001) {
                report.append(String.format("- %s: %.2f%%\n", 
                    entry.getKey().toString(), entry.getValue() * 100));
            }
        }
        
        List<BlockPos> bestSpots = predictNextBestMiningSpots(playerPos, 20, 5);
        report.append("\nTop 5 recommended mining spots:\n");
        for (int i = 0; i < bestSpots.size(); i++) {
            BlockPos spot = bestSpots.get(i);
            double score = predictOreChance(spot, Blocks.DIAMOND_ORE);
            report.append(String.format("%d. %s (Score: %.2f%%)\n", 
                i + 1, spot.toShortString(), score * 100));
        }
        
        return report.toString();
    }
}