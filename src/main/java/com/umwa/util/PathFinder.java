package com.umwa.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.*;

public class PathFinder {
    
    public List<BlockPos> findOptimalPath(BlockPos start, BlockPos target, World world) {
        // Simple A* pathfinding implementation
        Set<BlockPos> visited = new HashSet<>();
        PriorityQueue<PathNode> openSet = new PriorityQueue<>(Comparator.comparing(PathNode::getFCost));
        Map<BlockPos, PathNode> allNodes = new HashMap<>();
        
        PathNode startNode = new PathNode(start, 0, heuristic(start, target), null);
        openSet.add(startNode);
        allNodes.put(start, startNode);
        
        while (!openSet.isEmpty()) {
            PathNode current = openSet.poll();
            
            if (current.pos.equals(target)) {
                return reconstructPath(current);
            }
            
            visited.add(current.pos);
            
            for (BlockPos neighbor : getNeighbors(current.pos)) {
                if (visited.contains(neighbor) || !isPassable(neighbor, world)) {
                    continue;
                }
                
                double tentativeG = current.gCost + getMoveCost(current.pos, neighbor, world);
                PathNode neighborNode = allNodes.get(neighbor);
                
                if (neighborNode == null) {
                    neighborNode = new PathNode(neighbor, tentativeG, heuristic(neighbor, target), current);
                    allNodes.put(neighbor, neighborNode);
                    openSet.add(neighborNode);
                } else if (tentativeG < neighborNode.gCost) {
                    neighborNode.gCost = tentativeG;
                    neighborNode.parent = current;
                }
            }
        }
        
        return Collections.emptyList(); // No path found
    }
    
    private List<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> neighbors = new ArrayList<>();
        
        // Add 6-directional neighbors
        neighbors.add(pos.north());
        neighbors.add(pos.south());
        neighbors.add(pos.east());
        neighbors.add(pos.west());
        neighbors.add(pos.up());
        neighbors.add(pos.down());
        
        return neighbors;
    }
    
    private boolean isPassable(BlockPos pos, World world) {
        if (world == null) return true; // Default to passable if world is not available
        
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.WATER;
    }
    
    private double getMoveCost(BlockPos from, BlockPos to, World world) {
        double baseCost = from.getSquaredDistance(to);
        
        if (world != null) {
            Block block = world.getBlockState(to).getBlock();
            
            // Add cost penalties for dangerous blocks
            if (block == Blocks.LAVA) return baseCost + 1000; // Avoid lava
            if (block == Blocks.WATER) return baseCost + 5; // Slight penalty for water
        }
        
        return baseCost;
    }
    
    private double heuristic(BlockPos a, BlockPos b) {
        // Manhattan distance
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) + Math.abs(a.getZ() - b.getZ());
    }
    
    private List<BlockPos> reconstructPath(PathNode endNode) {
        List<BlockPos> path = new ArrayList<>();
        PathNode current = endNode;
        
        while (current != null) {
            path.add(0, current.pos);
            current = current.parent;
        }
        
        return path;
    }
    
    private static class PathNode {
        final BlockPos pos;
        double gCost;
        final double hCost;
        PathNode parent;
        
        PathNode(BlockPos pos, double gCost, double hCost, PathNode parent) {
            this.pos = pos;
            this.gCost = gCost;
            this.hCost = hCost;
            this.parent = parent;
        }
        
        double getFCost() {
            return gCost + hCost;
        }
    }
}