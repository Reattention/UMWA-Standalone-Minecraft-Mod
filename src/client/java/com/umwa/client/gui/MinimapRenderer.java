package com.umwa.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import com.umwa.UMWAMod;
import com.umwa.data.CaveSystem;
import com.umwa.data.HotSpot;
import java.util.Map;
import java.util.List;

public class MinimapRenderer {
    private final MinecraftClient client;
    private boolean isVisible = true;
    private final int mapSize = 100;
    private final int mapScale = 2;
    
    public MinimapRenderer() {
        this.client = MinecraftClient.getInstance();
    }
    
    public void render(DrawContext context, float tickDelta) {
        if (!isVisible || client.player == null) return;
        
        int screenWidth = client.getWindow().getScaledWidth();
        int mapX = screenWidth - mapSize - 10;
        int mapY = 10;
        
        // Draw minimap background
        context.fill(mapX - 2, mapY - 2, mapX + mapSize + 2, mapY + mapSize + 2, 0x80000000);
        context.fill(mapX, mapY, mapX + mapSize, mapY + mapSize, 0x40000000);
        
        // Get player position
        BlockPos playerPos = client.player.getBlockPos();
        int centerX = mapX + mapSize / 2;
        int centerY = mapY + mapSize / 2;
        
        // Draw cave systems
        renderCaveSystems(context, playerPos, centerX, centerY);
        
        // Draw hotspots
        renderHotSpots(context, playerPos, centerX, centerY);
        
        // Draw player marker
        context.fill(centerX - 1, centerY - 1, centerX + 2, centerY + 2, 0xFFFF0000);
        
        // Draw compass directions
        context.drawTextWithShadow(client.textRenderer, "N", centerX - 3, mapY - 15, 0xFFFFFFFF);
    }
    
    private void renderCaveSystems(DrawContext context, BlockPos playerPos, int centerX, int centerY) {
        Map<ChunkPos, CaveSystem> caveSystems = UMWAMod.getWorldAnalyzer().getCaveSystems();
        
        for (CaveSystem cave : caveSystems.values()) {
            BlockPos caveCenter = cave.getCenter();
            int relativeX = caveCenter.getX() - playerPos.getX();
            int relativeZ = caveCenter.getZ() - playerPos.getZ();
            
            // Scale to minimap coordinates
            int mapX = centerX + (relativeX / mapScale);
            int mapZ = centerY + (relativeZ / mapScale);
            
            // Only draw if within minimap bounds
            if (mapX >= centerX - mapSize/2 && mapX <= centerX + mapSize/2 && 
                mapZ >= centerY - mapSize/2 && mapZ <= centerY + mapSize/2) {
                
                int color = cave.isLarge() ? 0xFF4444FF : 0xFF6666FF; // Blue for caves, darker for large caves
                int size = Math.max(1, cave.getVolume() / 100);
                size = Math.min(size, 4); // Limit marker size
                
                context.fill(mapX - size, mapZ - size, mapX + size + 1, mapZ + size + 1, color);
            }
        }
    }
    
    private void renderHotSpots(DrawContext context, BlockPos playerPos, int centerX, int centerY) {
        List<HotSpot> hotSpots = UMWAMod.getWorldAnalyzer().getHotSpots();
        
        for (HotSpot hotSpot : hotSpots) {
            BlockPos hotSpotPos = hotSpot.getCenter();
            int relativeX = hotSpotPos.getX() - playerPos.getX();
            int relativeZ = hotSpotPos.getZ() - playerPos.getZ();
            
            // Scale to minimap coordinates
            int mapX = centerX + (relativeX / mapScale);
            int mapZ = centerY + (relativeZ / mapScale);
            
            // Only draw if within minimap bounds
            if (mapX >= centerX - mapSize/2 && mapX <= centerX + mapSize/2 && 
                mapZ >= centerY - mapSize/2 && mapZ <= centerY + mapSize/2) {
                
                // Color based on hotspot value
                int color;
                if (hotSpot.getValueScore() > 500) {
                    color = 0xFFFFD700; // Gold for high-value
                } else if (hotSpot.getValueScore() > 200) {
                    color = 0xFFFFA500; // Orange for medium-value
                } else {
                    color = 0xFFFFFF00; // Yellow for low-value
                }
                
                int size = Math.max(2, hotSpot.getTotalOres() / 5);
                size = Math.min(size, 3);
                
                context.fill(mapX - size, mapZ - size, mapX + size + 1, mapZ + size + 1, color);
            }
        }
    }
    
    public void tick() {
        // Update minimap data
    }
    
    public void toggleVisibility() {
        isVisible = !isVisible;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
}