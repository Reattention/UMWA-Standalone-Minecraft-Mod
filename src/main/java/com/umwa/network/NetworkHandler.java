package com.umwa.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import com.umwa.UMWAMod;

public class NetworkHandler {
    public static final Identifier MINING_DATA_SYNC = new Identifier(UMWAMod.MOD_ID, "mining_data_sync");
    public static final Identifier CAVE_DATA_SYNC = new Identifier(UMWAMod.MOD_ID, "cave_data_sync");
    public static final Identifier HOTSPOT_DATA_SYNC = new Identifier(UMWAMod.MOD_ID, "hotspot_data_sync");
    
    public static void registerServerPackets() {
        // Register server-side packet handlers
        UMWAMod.LOGGER.info("Registering server network packets");
    }
    
    public static void registerClientPackets() {
        // Register client-side packet handlers
        ClientPlayNetworking.registerGlobalReceiver(MINING_DATA_SYNC, (client, handler, buf, responseSender) -> {
            // Handle mining data synchronization from server
        });
        
        ClientPlayNetworking.registerGlobalReceiver(CAVE_DATA_SYNC, (client, handler, buf, responseSender) -> {
            // Handle cave system data synchronization from server
        });
        
        ClientPlayNetworking.registerGlobalReceiver(HOTSPOT_DATA_SYNC, (client, handler, buf, responseSender) -> {
            // Handle hotspot data synchronization from server
        });
        
        UMWAMod.LOGGER.info("Registering client network packets");
    }
}