package com.umwa.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import com.umwa.UMWAMod;
import com.umwa.client.gui.MiningHUD;
import com.umwa.client.gui.MinimapRenderer;
import com.umwa.client.input.KeyBindings;
import com.umwa.network.NetworkHandler;

public class UMWAModClient implements ClientModInitializer {
    private static MiningHUD miningHUD;
    private static MinimapRenderer minimapRenderer;
    
    @Override
    public void onInitializeClient() {
        UMWAMod.LOGGER.info("Initializing UMWA Mod Client");
        
        // Initialize client components
        miningHUD = new MiningHUD();
        minimapRenderer = new MinimapRenderer();
        
        // Register key bindings
        KeyBindings.initialize();
        
        // Register network handlers
        NetworkHandler.registerClientPackets();
        
        // Register HUD rendering
        HudRenderCallback.EVENT.register(miningHUD::render);
        HudRenderCallback.EVENT.register(minimapRenderer::render);
        
        // Register client tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            miningHUD.tick();
            minimapRenderer.tick();
        });
        
        UMWAMod.LOGGER.info("UMWA Mod Client initialized successfully");
    }
    
    public static MiningHUD getMiningHUD() {
        return miningHUD;
    }
    
    public static MinimapRenderer getMinimapRenderer() {
        return minimapRenderer;
    }
}