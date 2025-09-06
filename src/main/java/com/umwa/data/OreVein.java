package com.umwa.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import java.util.HashSet;
import java.util.Set;

public class OreVein {
    private final Block oreType;
    private final Set<BlockPos> oreBlocks;
    private final BlockPos origin;
    private int size;
    
    public OreVein(Block oreType, BlockPos origin) {
        this.oreType = oreType;
        this.origin = origin;
        this.oreBlocks = new HashSet<>();
        this.oreBlocks.add(origin);
        this.size = 1;
    }
    
    public void addOreBlock(BlockPos pos) {
        if (oreBlocks.add(pos)) {
            size++;
        }
    }
    
    public boolean isNearby(BlockPos pos, int maxDistance) {
        return oreBlocks.stream()
                .anyMatch(orePos -> orePos.isWithinDistance(pos, maxDistance));
    }
    
    public Block getOreType() {
        return oreType;
    }
    
    public Set<BlockPos> getOreBlocks() {
        return new HashSet<>(oreBlocks);
    }
    
    public BlockPos getOrigin() {
        return origin;
    }
    
    public int getSize() {
        return size;
    }
    
    public BlockPos getCenter() {
        if (oreBlocks.isEmpty()) return origin;
        
        int avgX = (int) oreBlocks.stream().mapToInt(BlockPos::getX).average().orElse(origin.getX());
        int avgY = (int) oreBlocks.stream().mapToInt(BlockPos::getY).average().orElse(origin.getY());
        int avgZ = (int) oreBlocks.stream().mapToInt(BlockPos::getZ).average().orElse(origin.getZ());
        
        return new BlockPos(avgX, avgY, avgZ);
    }
    
    public double getDensity() {
        if (oreBlocks.size() <= 1) return 1.0;
        
        // Calculate approximate volume and density
        int minX = oreBlocks.stream().mapToInt(BlockPos::getX).min().orElse(origin.getX());
        int maxX = oreBlocks.stream().mapToInt(BlockPos::getX).max().orElse(origin.getX());
        int minY = oreBlocks.stream().mapToInt(BlockPos::getY).min().orElse(origin.getY());
        int maxY = oreBlocks.stream().mapToInt(BlockPos::getY).max().orElse(origin.getY());
        int minZ = oreBlocks.stream().mapToInt(BlockPos::getZ).min().orElse(origin.getZ());
        int maxZ = oreBlocks.stream().mapToInt(BlockPos::getZ).max().orElse(origin.getZ());
        
        int volume = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        return volume > 0 ? (double) oreBlocks.size() / volume : 1.0;
    }
}