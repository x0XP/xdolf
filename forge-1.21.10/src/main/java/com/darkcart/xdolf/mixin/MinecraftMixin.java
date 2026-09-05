package com.darkcart.xdolf.mixin;

import com.darkcart.xdolf.Hooks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow private int rightClickDelay;
    @Inject(method = "tick", at = @At("HEAD"))
    private void xdolf$fastPlace(CallbackInfo ci) { if (Hooks.active("FastPlace")) rightClickDelay = 0; }
    @Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
    private void xdolf$entityOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (Hooks.enabled("EntityESP") && Hooks.highlight(entity)) cir.setReturnValue(true);
    }
}
