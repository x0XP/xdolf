package com.darkcart.xdolf.mixin;

import com.darkcart.xdolf.Hooks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "getFriction", at = @At("HEAD"), cancellable = true)
    private void xdolf$iceFriction(CallbackInfoReturnable<Float> cir) {
        if (((Object) this == Blocks.ICE || (Object) this == Blocks.PACKED_ICE || (Object) this == Blocks.BLUE_ICE)
            && Hooks.active("NoSlowdown")) cir.setReturnValue(0.39f);
    }
}
