package com.umwa.data;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class CaveSystem {
    private final ChunkPos chunkPos;
    private final Set<BlockPos> airBlocks;
    private final Set<BlockPos> floorBlocks;
    private final Set<BlockPos> ceilingBlocks;
    private final List<BlockPos> entrances;
    private int volume;
    private boolean isLarge;
    
    public CaveSystem(ChunkPos chunkPos) {
        this.chunkPos = chunkPos;
        this.airBlocks = new HashSet<>();
        this.floorBlocks = new HashSet<>();
        this.ceilingBlocks = new HashSet<>();
        this.entrances = new ArrayList<>();
        this.volume = 0;
        this.isLarge = false;
    }
    
    public void addAirBlock(BlockPos pos) {
        if (airBlocks.add(pos)) {
            volume++;
            updateCaveProperties(pos);
        }
    }
    
    private void updateCaveProperties(BlockPos airPos) {
        // Check if this is a floor, ceiling, or entrance
        BlockPos below = airPos.down();
        BlockPos above = airPos.up();
        
        // Simple heuristics for cave structure
        if (isGroundBlock(below)) {
            floorBlocks.add(below);
        }
        
        if (isGroundBlock(above)) {
            ceilingBlocks.add(above);
        }
        
        // Check if this might be an entrance (air block at surface level)
        if (airPos.getY() > 60 && airPos.getY() < 70) {
            entrances.add(airPos);
        }
        
        // Mark as large cave if volume exceeds threshold
        if (volume > 1000) {
            isLarge = true;
        }
    }
    
    private boolean isGroundBlock(BlockPos pos) {
        // This would check if the block is solid ground
        // For now, just return true for simplicity
        return true;
    }
    
    public ChunkPos getChunkPos() {
        return chunkPos;
    }
    
    public Set<BlockPos> getAirBlocks() {
        return new HashSet<>(airBlocks);
    }
    
    public Set<BlockPos> getFloorBlocks() {
        return new HashSet<>(floorBlocks);
    }
    
    public Set<BlockPos> getCeilingBlocks() {
        return new HashSet<>(ceilingBlocks);
    }
    
    public List<BlockPos> getEntrances() {
        return new ArrayList<>(entrances);
    }
    
    public int getVolume() {
        return volume;
    }
    
    public boolean isLarge() {
        return isLarge;
    }
    
    public BlockPos getCenter() {
        if (airBlocks.isEmpty()) return new BlockPos(chunkPos.x * 16 + 8, 64, chunkPos.z * 16 + 8);
        
        int avgX = (int) airBlocks.stream().mapToInt(BlockPos::getX).average().orElse(chunkPos.x * 16 + 8);
        int avgY = (int) airBlocks.stream().mapToInt(BlockPos::getY).average().orElse(64);
        int avgZ = (int) airBlocks.stream().mapToInt(BlockPos::getZ).average().orElse(chunkPos.z * 16 + 8);
        
        return new BlockPos(avgX, avgY, avgZ);
    }
    
    public int getDepth() {
        if (airBlocks.isEmpty()) return 0;
        
        int minY = airBlocks.stream().mapToInt(BlockPos::getY).min().orElse(0);
        int maxY = airBlocks.stream().mapToInt(BlockPos::getY).max().orElse(0);
        
        return maxY - minY;
    }
}