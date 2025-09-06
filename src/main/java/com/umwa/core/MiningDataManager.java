package com.umwa.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.umwa.data.MiningSession;
import com.umwa.data.OreVein;
import com.umwa.data.WorldData;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiningDataManager {
    private final Map<String, WorldData> worldDataMap = new ConcurrentHashMap<>();
    private final Map<ChunkPos, Set<BlockPos>> exploredBlocks = new ConcurrentHashMap<>();
    private final Map<Block, Integer> oreCount = new ConcurrentHashMap<>();
    private final List<OreVein> discoveredVeins = new ArrayList<>();
    private MiningSession currentSession;
    
    public MiningDataManager() {
        this.currentSession = new MiningSession();
    }
    
    public void onBlockMined(BlockPos pos, Block block) {
        // Track mined block
        ChunkPos chunkPos = new ChunkPos(pos);
        exploredBlocks.computeIfAbsent(chunkPos, k -> new HashSet<>()).add(pos);
        
        // Track ores
        if (isOreBlock(block)) {
            oreCount.merge(block, 1, Integer::sum);
            currentSession.addOreFound(block);
            
            // Check for ore vein
            detectOreVein(pos, block);
        }
        
        currentSession.incrementBlocksMined();
    }
    
    public void onBlockPlaced(BlockPos pos, Block block) {
        // Track placed blocks for analysis
        ChunkPos chunkPos = new ChunkPos(pos);
        exploredBlocks.computeIfAbsent(chunkPos, k -> new HashSet<>()).add(pos);
    }
    
    private boolean isOreBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        String path = id.getPath();
        return path.contains("ore") || 
               path.contains("diamond") || 
               path.contains("emerald") || 
               path.contains("ancient_debris");
    }
    
    private void detectOreVein(BlockPos pos, Block block) {
        // Simple ore vein detection - check if this ore connects to existing veins
        for (OreVein vein : discoveredVeins) {
            if (vein.getOreType() == block && vein.isNearby(pos, 3)) {
                vein.addOreBlock(pos);
                return;
            }
        }
        
        // Create new vein
        OreVein newVein = new OreVein(block, pos);
        discoveredVeins.add(newVein);
    }
    
    public void tick() {
        // Update mining efficiency calculations
        currentSession.updateEfficiency();
    }
    
    public WorldData getWorldData(String worldName) {
        return worldDataMap.computeIfAbsent(worldName, WorldData::new);
    }
    
    public MiningSession getCurrentSession() {
        return currentSession;
    }
    
    public Map<Block, Integer> getOreCount() {
        return new HashMap<>(oreCount);
    }
    
    public List<OreVein> getDiscoveredVeins() {
        return new ArrayList<>(discoveredVeins);
    }
    
    public Set<ChunkPos> getExploredChunks() {
        return exploredBlocks.keySet();
    }
    
    public double getMiningEfficiency() {
        return currentSession.getEfficiency();
    }
    
    public int getTotalBlocksMined() {
        return currentSession.getBlocksMined();
    }
    
    public int getTotalOresFound() {
        return currentSession.getTotalOres();
    }
}