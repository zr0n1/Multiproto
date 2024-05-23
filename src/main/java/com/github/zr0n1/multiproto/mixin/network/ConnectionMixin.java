package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.protocol.packet.PacketHandlerRegistry;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Redirect(method = "sendPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int sendPacketWithHandlerSize(Packet packet) {
        return PacketHandlerRegistry.size(packet);
    }

    @Redirect(method = "method_1137", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int quinkstanty(Packet packet) {
        return PacketHandlerRegistry.size(packet);
    }

    @Redirect(method = "method_1139", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int ploinkus(Packet packet) {
        return PacketHandlerRegistry.size(packet);
    }
}
