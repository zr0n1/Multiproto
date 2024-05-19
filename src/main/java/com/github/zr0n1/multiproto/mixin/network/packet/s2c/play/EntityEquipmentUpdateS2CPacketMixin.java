package com.github.zr0n1.multiproto.mixin.network.packet.s2c.play;

import com.github.zr0n1.multiproto.Utils;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;

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

    @Redirect(method = "read", at = @At(value = "INVOKE",
            target = "Ljava/io/DataInputStream;readShort()S", ordinal = 2))
    private short redirectReadDamage(DataInputStream stream) throws IOException {
        return (Utils.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0) ? stream.readShort() : 0;
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V"),
            slice = @Slice(from = @At(value = "FIELD",
                    target = "Lnet/minecraft/network/packet/s2c/play/EntityEquipmentUpdateS2CPacket;itemRawId:I")))
    private void redirectWriteDamage(DataOutputStream stream, int i) throws IOException {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0) stream.writeShort(itemDamage);
    }
}
