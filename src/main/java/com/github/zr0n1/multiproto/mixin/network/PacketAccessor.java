package com.github.zr0n1.multiproto.mixin.network;

import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(Packet.class)
public interface PacketAccessor {
    @Accessor
    static Set<Integer> getClientBoundPackets() {
        return Util.assertMixin();
    }

    @Accessor
    static Set<Integer> getServerBoundPackets() {
        return Util.assertMixin();
    }
}
