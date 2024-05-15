package com.github.zr0n1.multiproto.mixin.network.packet.s2c.play;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(EntityEquipmentUpdateS2CPacket.class)
public abstract class EntityEquipmentUpdateS2CPacketMixin {

    @Shadow public int itemDamage;

    @Redirect(method = "read", at = @At(value = "FIELD",
            target = "Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;itemDamage:I", opcode = Opcodes.PUTFIELD))
    private void redirectReadDamage(EntityEquipmentUpdateS2CPacket packet, int value, @Local DataInputStream stream) throws IOException {
        packet.itemDamage = ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_8) >= 0 ? stream.readShort() : 0;
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;itemRawId:I",
            opcode = Opcodes.GETFIELD)))
    private void redirectWriteDamage(DataOutputStream stream, int i) throws IOException {
        if(ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_8) >= 0) stream.writeShort(itemDamage);
    }
}
