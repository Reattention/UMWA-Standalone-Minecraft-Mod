package com.umwa.client.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import com.umwa.client.UMWAModClient;

public class KeyBindings {
    public static KeyBinding toggleHUD;
    public static KeyBinding toggleMinimap;
    public static KeyBinding openMiningMenu;
    
    public static void initialize() {
        toggleHUD = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.umwa.toggle_hud",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "category.umwa.general"
        ));
        
        toggleMinimap = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.umwa.toggle_minimap",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "category.umwa.general"
        ));
        
        openMiningMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.umwa.open_menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "category.umwa.general"
        ));
    }
    
    public static void handleInput() {
        while (toggleHUD.wasPressed()) {
            if (UMWAModClient.getMiningHUD() != null) {
                UMWAModClient.getMiningHUD().toggleVisibility();
            }
        }
        
        while (toggleMinimap.wasPressed()) {
            if (UMWAModClient.getMinimapRenderer() != null) {
                UMWAModClient.getMinimapRenderer().toggleVisibility();
            }
        }
        
        while (openMiningMenu.wasPressed()) {
            // TODO: Open mining analysis menu
        }
    }
}