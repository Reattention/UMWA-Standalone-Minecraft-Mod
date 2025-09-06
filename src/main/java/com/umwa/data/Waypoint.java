package com.umwa.data;

import net.minecraft.util.math.BlockPos;
import java.util.*;

public class Waypoint {
    private final String name;
    private final BlockPos position;
    private final WaypointType type;
    private final String description;
    private final long createdTime;
    private final int color;
    private boolean isVisible;
    private boolean isAlerted;
    
    public enum WaypointType {
        DIAMOND_VEIN(0xFF00FFFF, "Diamond Vein"),
        EMERALD_VEIN(0xFF00FF00, "Emerald Vein"),
        CAVE_ENTRANCE(0xFF8B4513, "Cave Entrance"),
        LAVA_WARNING(0xFFFF4500, "Lava Hazard"),
        WATER_SOURCE(0xFF0000FF, "Water Source"),
        MOB_SPAWNER(0xFF800080, "Mob Spawner"),
        VILLAGE(0xFFFFFF00, "Village"),
        STRONGHOLD(0xFF4B0082, "Stronghold"),
        DUNGEON(0xFF696969, "Dungeon"),
        HOTSPOT(0xFFFFD700, "Mining Hotspot"),
        BASE_CAMP(0xFF32CD32, "Base Camp"),
        CUSTOM(0xFFFFFFFF, "Custom");
        
        private final int color;
        private final String displayName;
        
        WaypointType(int color, String displayName) {
            this.color = color;
            this.displayName = displayName;
        }
        
        public int getColor() { return color; }
        public String getDisplayName() { return displayName; }
    }
    
    public Waypoint(String name, BlockPos position, WaypointType type, String description) {
        this.name = name;
        this.position = position;
        this.type = type;
        this.description = description;
        this.createdTime = System.currentTimeMillis();
        this.color = type.getColor();
        this.isVisible = true;
        this.isAlerted = false;
    }
    
    public Waypoint(String name, BlockPos position, WaypointType type) {
        this(name, position, type, "");
    }
    
    public String getName() { return name; }
    public BlockPos getPosition() { return position; }
    public WaypointType getType() { return type; }
    public String getDescription() { return description; }
    public long getCreatedTime() { return createdTime; }
    public int getColor() { return color; }
    public boolean isVisible() { return isVisible; }
    public boolean isAlerted() { return isAlerted; }
    
    public void setVisible(boolean visible) { this.isVisible = visible; }
    public void setAlerted(boolean alerted) { this.isAlerted = alerted; }
    
    public double getDistanceFrom(BlockPos pos) {
        return Math.sqrt(position.getSquaredDistance(pos));
    }
    
    public String getDirectionFrom(BlockPos pos) {
        int dx = position.getX() - pos.getX();
        int dz = position.getZ() - pos.getZ();
        
        if (Math.abs(dx) > Math.abs(dz)) {
            return dx > 0 ? "East" : "West";
        } else {
            return dz > 0 ? "South" : "North";
        }
    }
    
    public String getFormattedInfo() {
        return String.format("%s (%s) at %s", 
            name, type.getDisplayName(), position.toShortString());
    }
    
    public boolean isNearby(BlockPos pos, int radius) {
        return position.isWithinDistance(pos, radius);
    }
    
    public long getAge() {
        return System.currentTimeMillis() - createdTime;
    }
    
    public String getAgeString() {
        long ageMs = getAge();
        long minutes = ageMs / 60000;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) return days + " days ago";
        if (hours > 0) return hours + " hours ago";
        if (minutes > 0) return minutes + " minutes ago";
        return "Just now";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Waypoint waypoint = (Waypoint) obj;
        return Objects.equals(name, waypoint.name) && 
               Objects.equals(position, waypoint.position);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, position);
    }
    
    @Override
    public String toString() {
        return getFormattedInfo();
    }
}