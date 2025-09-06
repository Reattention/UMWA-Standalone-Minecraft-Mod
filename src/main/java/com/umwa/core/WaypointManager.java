package com.umwa.core;

import net.minecraft.util.math.BlockPos;
import com.umwa.data.Waypoint;
import java.util.*;
import java.util.stream.Collectors;

public class WaypointManager {
    private final Map<String, Waypoint> waypoints = new HashMap<>();
    private final Map<Waypoint.WaypointType, List<Waypoint>> waypointsByType = new HashMap<>();
    private final Set<String> alertedWaypoints = new HashSet<>();
    
    public WaypointManager() {
        // Initialize type maps
        for (Waypoint.WaypointType type : Waypoint.WaypointType.values()) {
            waypointsByType.put(type, new ArrayList<>());
        }
    }
    
    public void addWaypoint(Waypoint waypoint) {
        waypoints.put(waypoint.getName(), waypoint);
        waypointsByType.get(waypoint.getType()).add(waypoint);
    }
    
    public void addWaypoint(String name, BlockPos pos, Waypoint.WaypointType type) {
        addWaypoint(new Waypoint(name, pos, type));
    }
    
    public void addWaypoint(String name, BlockPos pos, Waypoint.WaypointType type, String description) {
        addWaypoint(new Waypoint(name, pos, type, description));
    }
    
    public void removeWaypoint(String name) {
        Waypoint waypoint = waypoints.remove(name);
        if (waypoint != null) {
            waypointsByType.get(waypoint.getType()).remove(waypoint);
            alertedWaypoints.remove(name);
        }
    }
    
    public Waypoint getWaypoint(String name) {
        return waypoints.get(name);
    }
    
    public List<Waypoint> getAllWaypoints() {
        return new ArrayList<>(waypoints.values());
    }
    
    public List<Waypoint> getWaypointsByType(Waypoint.WaypointType type) {
        return new ArrayList<>(waypointsByType.get(type));
    }
    
    public List<Waypoint> getNearbyWaypoints(BlockPos playerPos, int radius) {
        return waypoints.values().stream()
            .filter(w -> w.isNearby(playerPos, radius))
            .collect(Collectors.toList());
    }
    
    public List<Waypoint> getVisibleWaypoints() {
        return waypoints.values().stream()
            .filter(Waypoint::isVisible)
            .collect(Collectors.toList());
    }
    
    public Waypoint getNearestWaypoint(BlockPos pos, Waypoint.WaypointType type) {
        return waypointsByType.get(type).stream()
            .min(Comparator.comparing(w -> w.getDistanceFrom(pos)))
            .orElse(null);
    }
    
    public void toggleWaypointVisibility(String name) {
        Waypoint waypoint = waypoints.get(name);
        if (waypoint != null) {
            waypoint.setVisible(!waypoint.isVisible());
        }
    }
    
    public void setWaypointAlert(String name, boolean alert) {
        Waypoint waypoint = waypoints.get(name);
        if (waypoint != null) {
            waypoint.setAlerted(alert);
            if (alert) {
                alertedWaypoints.add(name);
            } else {
                alertedWaypoints.remove(name);
            }
        }
    }
    
    public List<String> checkProximityAlerts(BlockPos playerPos, int alertRadius) {
        List<String> alerts = new ArrayList<>();
        
        for (Waypoint waypoint : waypoints.values()) {
            if (waypoint.isNearby(playerPos, alertRadius) && 
                waypoint.isVisible() && 
                !alertedWaypoints.contains(waypoint.getName())) {
                
                alerts.add(String.format("Approaching %s: %s (%.1f blocks away)", 
                    waypoint.getType().getDisplayName(),
                    waypoint.getName(),
                    waypoint.getDistanceFrom(playerPos)));
                
                setWaypointAlert(waypoint.getName(), true);
            }
        }
        
        return alerts;
    }
    
    public void autoCreateWaypoint(BlockPos pos, Waypoint.WaypointType type, String baseName) {
        // Generate unique name
        String name = baseName;
        int counter = 1;
        while (waypoints.containsKey(name)) {
            name = baseName + " " + counter++;
        }
        
        addWaypoint(name, pos, type);
    }
    
    public Map<Waypoint.WaypointType, Integer> getWaypointCounts() {
        Map<Waypoint.WaypointType, Integer> counts = new HashMap<>();
        for (Waypoint.WaypointType type : Waypoint.WaypointType.values()) {
            counts.put(type, waypointsByType.get(type).size());
        }
        return counts;
    }
    
    public List<Waypoint> getRecentWaypoints(int hours) {
        long cutoffTime = System.currentTimeMillis() - (hours * 3600000L);
        return waypoints.values().stream()
            .filter(w -> w.getCreatedTime() >= cutoffTime)
            .sorted(Comparator.comparing(Waypoint::getCreatedTime).reversed())
            .collect(Collectors.toList());
    }
    
    public void clearOldWaypoints(int daysOld) {
        long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 3600000L);
        List<String> toRemove = waypoints.values().stream()
            .filter(w -> w.getCreatedTime() < cutoffTime)
            .map(Waypoint::getName)
            .collect(Collectors.toList());
        
        toRemove.forEach(this::removeWaypoint);
    }
    
    public String generateWaypointSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Waypoint Summary\n");
        summary.append("===============\n");
        summary.append("Total waypoints: ").append(waypoints.size()).append("\n\n");
        
        Map<Waypoint.WaypointType, Integer> counts = getWaypointCounts();
        for (Map.Entry<Waypoint.WaypointType, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > 0) {
                summary.append(String.format("- %s: %d\n", 
                    entry.getKey().getDisplayName(), entry.getValue()));
            }
        }
        
        List<Waypoint> recent = getRecentWaypoints(24);
        if (!recent.isEmpty()) {
            summary.append("\nRecent waypoints (last 24 hours):\n");
            for (Waypoint waypoint : recent.subList(0, Math.min(5, recent.size()))) {
                summary.append(String.format("- %s (%s)\n", 
                    waypoint.getName(), waypoint.getAgeString()));
            }
        }
        
        return summary.toString();
    }
}