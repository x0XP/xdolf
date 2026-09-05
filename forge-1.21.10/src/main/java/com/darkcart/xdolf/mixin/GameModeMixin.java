package com.darkcart.xdolf.mixin;

import com.darkcart.xdolf.Hooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class GameModeMixin {
    @Shadow private int destroyDelay;
    @Shadow private float destroyProgress;
    @Shadow private boolean isDestroying;
    @Shadow private BlockPos destroyBlockPos;
    @Inject(method = "continueDestroyBlock", at = @At("HEAD"))
    private void xdolf$speedmine(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (!Hooks.active("Speedmine")) return;
        destroyDelay = 0;
        var mc = Minecraft.getInstance();
        if (isDestroying && pos.equals(destroyBlockPos)) {
            float step = mc.level.getBlockState(pos).getDestroyProgress(mc.player, mc.level, pos);
            destroyProgress += step * (float) (Hooks.setting("Speedmine", "multiplier", 2) - 1);
        }
    }
}
