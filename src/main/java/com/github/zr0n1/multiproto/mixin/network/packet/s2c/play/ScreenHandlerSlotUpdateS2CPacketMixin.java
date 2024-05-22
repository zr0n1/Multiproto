package com.github.zr0n1.multiproto.mixin.network.packet.s2c.play;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(ScreenHandlerSlotUpdateS2CPacket.class)
public abstract class ScreenHandlerSlotUpdateS2CPacketMixin {

    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readShort()S"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readByte()B", ordinal = 1)))
    private short redirectReadDamage(DataInputStream stream) throws IOException {
        return ProtocolVersionManager.isBefore(ProtocolVersion.BETA_8) ? stream.readByte() : stream.readShort();
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeByte(I)V", ordinal = 1)))
    private void redirectWriteDamage(DataOutputStream stream, int i) throws IOException {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_8)) stream.writeShort(i);
        else stream.writeByte(i);
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_8)) cir.setReturnValue(7);
    }
}
