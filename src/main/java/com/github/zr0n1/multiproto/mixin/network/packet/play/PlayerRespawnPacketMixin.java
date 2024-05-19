package com.github.zr0n1.multiproto.mixin.network.packet.play;

import com.github.zr0n1.multiproto.Utils;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;

import net.minecraft.network.packet.play.PlayerRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;

@Mixin(PlayerRespawnPacket.class)
public abstract class PlayerRespawnPacketMixin {

    @Inject(method = "read", at = @At("HEAD"), cancellable = true)
    private void read(DataInputStream stream, CallbackInfo ci) {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_13) < 0) ci.cancel();
    }

    @Inject(method = "write", at = @At("HEAD"), cancellable = true)
    private void write(DataOutputStream stream, CallbackInfo ci) {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_13) < 0) ci.cancel();
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_13) < 0) cir.setReturnValue(0);
    }
}
