package com.umwa;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import com.umwa.core.MiningDataManager;
import com.umwa.core.WorldAnalyzer;
import com.umwa.network.NetworkHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UMWAMod implements ModInitializer {
    public static final String MOD_ID = "umwa-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MiningDataManager miningDataManager;
    private static WorldAnalyzer worldAnalyzer;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Ultimate Minecraft World Analyser");
        
        // Initialize core systems
        miningDataManager = new MiningDataManager();
        worldAnalyzer = new WorldAnalyzer();
        
        // Register network handlers
        NetworkHandler.registerServerPackets();
        
        // Register server tick events
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            miningDataManager.tick();
            worldAnalyzer.tick();
        });
        
        LOGGER.info("UMWA Mod initialized successfully");
    }
    
    public static MiningDataManager getMiningDataManager() {
        return miningDataManager;
    }
    
    public static WorldAnalyzer getWorldAnalyzer() {
        return worldAnalyzer;
    }
}