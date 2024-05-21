package com.github.zr0n1.multiproto.mixin.network.packet.c2s.play;

import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import org.objectweb.asm.Opcodes;
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
public abstract class ClickSlotC2SPacketMixin {

    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readShort()S", ordinal = 1),
            slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readBoolean()Z")))
    private short redirectReadDamage(DataInputStream stream) throws IOException {
        return ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0 ? stream.readShort() : stream.readByte();
    }

    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readBoolean()Z"))
    private boolean redirectReadHoldingShift(DataInputStream stream) throws IOException {
        return ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_11) >= 0 && stream.readBoolean();
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/item/ItemStack;count:I", opcode = Opcodes.GETFIELD)))
    private void redirectWriteDamage(DataOutputStream stream, int i) throws IOException {
        if(ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0) stream.writeShort(i);
        else stream.writeByte(i);
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeBoolean(Z)V"))
    private void redirectWriteHoldingShift(DataOutputStream stream, boolean b) throws IOException {
        if(ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_11) >= 0) stream.writeBoolean(b);
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if(ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) cir.setReturnValue(10);
    }
}
