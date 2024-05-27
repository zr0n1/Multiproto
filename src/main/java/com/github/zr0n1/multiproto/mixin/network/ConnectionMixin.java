package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.protocol.packet.PacketDataTranslator;
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void redirectPacket(Packet packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet> local) {
        local.set(PacketTranslator.redirect(packet));
    }

    @Redirect(method = "sendPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int sendPacketWithHandlerSize(Packet packet) {
        return PacketDataTranslator.size(packet);
    }

    @Redirect(method = "method_1137", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int quinkstanty(Packet packet) {
        return PacketDataTranslator.size(packet);
    }

    @Redirect(method = "method_1139", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int ploinkus(Packet packet) {
        return PacketDataTranslator.size(packet);
    }
}
