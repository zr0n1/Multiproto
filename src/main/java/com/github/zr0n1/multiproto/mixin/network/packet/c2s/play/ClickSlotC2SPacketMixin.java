package com.github.zr0n1.multiproto.mixin.network.packet.c2s.play;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(ClickSlotC2SPacket.class)
public abstract class ClickSlotC2SPacketMixin extends Packet {

    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readShort()S", ordinal = 1),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readBoolean()Z")))
    private short redirectReadShort(DataInputStream stream) throws IOException {
        return ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_8) >= 0 ?
                stream.readShort() : stream.readByte();
    }

    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readBoolean()Z"))
    private boolean redirectReadBoolean(DataInputStream stream) throws IOException {
        return ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_11) >= 0 ? stream.readBoolean() : false;
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeBoolean(Z)V"))
    private void redirectWriteBoolean(DataOutputStream stream, boolean b) throws IOException {
        if(ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_11) >= 0) stream.writeBoolean(b);
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if(ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_8) < 0) cir.setReturnValue(10);
    }
}
