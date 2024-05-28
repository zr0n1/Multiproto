package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = Connection.class, priority = 1001)
public abstract class ConnectionMixin {

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void wrapPacket(Packet packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet> packetRef) {
        if (PacketTranslator.isWrapped(packet.getRawId())) packetRef.set(PacketTranslator.wrap(packet));
    }

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void redirectPacket(Packet packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet> packetRef) {
        if (PacketTranslator.isRedirected(packet.getRawId())) packetRef.set(PacketTranslator.redirect(packet));
    }
}
