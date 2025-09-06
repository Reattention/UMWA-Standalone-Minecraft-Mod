package com.umwa.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import com.umwa.UMWAMod;
import com.umwa.data.MiningSession;

public class MiningHUD {
    private final MinecraftClient client;
    private boolean isVisible = true;
    
    public MiningHUD() {
        this.client = MinecraftClient.getInstance();
    }
    
    public void render(DrawContext context, float tickDelta) {
        if (!isVisible || client.player == null) return;
        
        TextRenderer textRenderer = client.textRenderer;
        int x = 10;
        int y = 10;
        int lineHeight = 12;
        
        // Get mining data
        MiningSession session = UMWAMod.getMiningDataManager().getCurrentSession();
        
        // Render mining statistics
        context.drawTextWithShadow(textRenderer, 
            Text.literal("UMWA Mining Stats").formatted(Formatting.GOLD, Formatting.BOLD), 
            x, y, 0xFFFFFF);
        y += lineHeight + 5;
        
        context.drawTextWithShadow(textRenderer, 
            Text.literal("Blocks Mined: " + session.getBlocksMined()).formatted(Formatting.WHITE), 
            x, y, 0xFFFFFF);
        y += lineHeight;
        
        context.drawTextWithShadow(textRenderer, 
            Text.literal("Ores Found: " + session.getTotalOres()).formatted(Formatting.YELLOW), 
            x, y, 0xFFFFFF);
        y += lineHeight;
        
        context.drawTextWithShadow(textRenderer, 
            Text.literal("Efficiency: " + String.format("%.2f%%", session.getEfficiency() * 100)).formatted(Formatting.GREEN), 
            x, y, 0xFFFFFF);
        y += lineHeight;
        
        context.drawTextWithShadow(textRenderer, 
            Text.literal("Blocks/Min: " + String.format("%.1f", session.getBlocksPerMinute())).formatted(Formatting.AQUA), 
            x, y, 0xFFFFFF);
        y += lineHeight + 5;
        
        // Render coordinates
        if (client.player != null) {
            int playerX = (int) client.player.getX();
            int playerY = (int) client.player.getY();
            int playerZ = (int) client.player.getZ();
            
            context.drawTextWithShadow(textRenderer, 
                Text.literal("Position: " + playerX + ", " + playerY + ", " + playerZ).formatted(Formatting.GRAY), 
                x, y, 0xFFFFFF);
            y += lineHeight;
            
            // Y-level mining recommendation
            String yLevelText = getYLevelRecommendation(playerY);
            context.drawTextWithShadow(textRenderer, 
                Text.literal(yLevelText).formatted(Formatting.LIGHT_PURPLE), 
                x, y, 0xFFFFFF);
        }
    }
    
    private String getYLevelRecommendation(int currentY) {
        if (currentY >= -16 && currentY <= -8) {
            return "Optimal Diamond Level!";
        } else if (currentY >= -64 && currentY <= 16) {
            return "Good mining depth";
        } else if (currentY > 16) {
            return "Go deeper for better ores";
        } else {
            return "Near bedrock - be careful!";
        }
    }
    
    public void tick() {
        // Update any dynamic HUD elements
    }
    
    public void toggleVisibility() {
        isVisible = !isVisible;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
}