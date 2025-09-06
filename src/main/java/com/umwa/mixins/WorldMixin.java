package com.umwa.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.umwa.UMWAMod;

@Mixin(World.class)
public class WorldMixin {
    
    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", 
            at = @At("HEAD"))
    private void onBlockChanged(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfo ci) {
        World world = (World) (Object) this;
        
        // Only track on server side
        if (!world.isClient) {
            Block newBlock = state.getBlock();
            BlockState oldState = world.getBlockState(pos);
            Block oldBlock = oldState.getBlock();
            
            // Track block mining (old block was not air, new block is air)
            if (!oldBlock.equals(newBlock)) {
                if (UMWAMod.getMiningDataManager() != null) {
                    UMWAMod.getMiningDataManager().onBlockMined(pos, oldBlock);
                }
                
                if (UMWAMod.getWorldAnalyzer() != null) {
                    UMWAMod.getWorldAnalyzer().setCurrentWorld(world);
                    UMWAMod.getWorldAnalyzer().analyzeArea(pos, 5);
                }
            }
        }
    }
}