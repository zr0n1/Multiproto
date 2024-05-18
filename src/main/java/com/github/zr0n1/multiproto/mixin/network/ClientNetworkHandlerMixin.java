package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.mixin.entity.EntityAccessor;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerMixin {

    @Inject(method = "onEntityAnimation", at = @At("RETURN"))
    private void onEntityAnimation(EntityAnimationPacket packet, CallbackInfo ci, @Local Entity e) {
        if(packet.animationId == 104 && e != null) ((EntityAccessor)e).invokeSetFlag(1, true);
        if(packet.animationId == 105 && e != null) ((EntityAccessor)e).invokeSetFlag(1, false);
    }
}
