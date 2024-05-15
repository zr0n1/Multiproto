package com.github.zr0n1.multiproto.mixin.network.packet.s2c.play;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;

@Mixin(EntitySpawnS2CPacket.class)
public abstract class EntitySpawnS2CPacketMixin extends Packet {
    @Inject(method = "read", at = @At(value = "FIELD", target = "Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;z:I",
    shift = At.Shift.AFTER), cancellable = true)
    private void read(DataInputStream stream, CallbackInfo ci) {
        if(ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_13) < 0) ci.cancel();
    }

    @Inject(method = "write", at = @At(value = "FIELD", target = "Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;z:I",
            shift = At.Shift.AFTER), cancellable = true)
    private void write(DataOutputStream stream, CallbackInfo ci) {
        if(ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_13) < 0) ci.cancel();
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if(ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_13) < 0) cir.setReturnValue(17);
    }
}
