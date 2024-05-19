package com.github.zr0n1.multiproto.mixin.network.packet.s2c.play;

import com.github.zr0n1.multiproto.Utils;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.LivingEntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntitySpawnS2CPacket.class)
public abstract class LivingEntitySpawnS2CPacketMixin {

    @Redirect(method = "read", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/entity/data/DataTracker;readEntries(Ljava/io/DataInputStream;)Ljava/util/List;"))
    private List redirectReadEntries(DataInputStream stream) {
        return Utils.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0 ?
                DataTracker.readEntries(stream) : new ArrayList();
    }

    @Redirect(method = "write", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/entity/data/DataTracker;writeAllEntries(Ljava/io/DataOutputStream;)V"))
    private void redirectWriteAllEntries(DataTracker tracker, DataOutputStream stream) {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0) tracker.writeAllEntries(stream);
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) cir.setReturnValue(19);
    }
}
