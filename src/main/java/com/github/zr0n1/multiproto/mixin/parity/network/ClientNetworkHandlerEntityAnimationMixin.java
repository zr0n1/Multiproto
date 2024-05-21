package com.github.zr0n1.multiproto.mixin.parity.network;

import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.github.zr0n1.multiproto.mixin.parity.entity.EntityAccessor;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
public abstract class ClientNetworkHandlerEntityAnimationMixin {

    @Inject(method = "onEntityAnimation", at = @At("TAIL"))
    private void applySneakingParity(EntityAnimationPacket packet, CallbackInfo ci, @Local Entity e) {
        if(ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) < 0 && e != null) {
            if(packet.animationId == 104) ((EntityAccessor) e).invokeSetFlag(1, true);
            if(packet.animationId == 105) ((EntityAccessor) e).invokeSetFlag(1, false);
        }
    }
}
