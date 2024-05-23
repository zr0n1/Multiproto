package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.github.zr0n1.multiproto.protocol.event.RegisterPacketHandlersListener;
import com.github.zr0n1.multiproto.protocol.packet.c2s.play.ClickSlotC2SPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.c2s.play.PlayerInteractBlockC2SPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.login.LoginHelloPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.s2c.play.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.network.packet.s2c.play.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class PacketHandlerRegistry {

    public static final Map<Integer, PacketHandler<? extends Packet>> HANDLERS = new HashMap<>();

    public static void read(Packet packet, DataInputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getRawId())) {
            Multiproto.LOGGER.info("reading id: {}, name: {} with handler", packet.getRawId(), packet.getClass().getName());
            HANDLERS.get(packet.getRawId()).readPacket(packet, stream);
        }
        else packet.read(stream);
    }

    public static void write(Packet packet, DataOutputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getRawId())) {
            Multiproto.LOGGER.info("writing id: {}, name: {} with handler", packet.getRawId(), packet.getClass().getName());
            HANDLERS.get(packet.getRawId()).writePacket(packet, stream);
        }
        else packet.write(stream);
    }

    public static int size(Packet packet) {
        return (HANDLERS.containsKey(packet.getRawId())) ? HANDLERS.get(packet.getRawId()).packetSize(packet) : packet.size();
    }

    public static void registerHandlers() {
        HANDLERS.clear();
        // < b1.7
        registerBefore(Version.BETA_14, 1, new LoginHelloPacketHandler());
        // < b1.6
        registerBefore(Version.BETA_13, 9, new EmptyPacketHandler<PlayerRespawnPacket>());
        registerBefore(Version.BETA_13, 23, new EntitySpawnS2CPacketHandler());
        // < b1.5
        registerBefore(Version.BETA_11, 102, new ClickSlotC2SPacketHandler());
        // < b1.2
        registerBefore(Version.BETA_8, 5, new EntityEquipmentUpdateS2CPacketHandler());
        registerBefore(Version.BETA_8, 15, new PlayerInteractBlockC2SPacketHandler());
        registerBefore(Version.BETA_8, 21, new ItemEntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_8, 24, new LivingEntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_8, 103, new ScreenHandlerSlotUpdateS2CPacketHandler());
        // custom packet handlers
        FabricLoader.getInstance().getEntrypointContainers("multiproto:register_packet_handlers", RegisterPacketHandlersListener.class)
                .forEach(listener -> listener.getEntrypoint().registerHandlers());
    }

    public static void registerBefore(Version version, int id, PacketHandler<? extends Packet> handler) {
        if (VersionManager.isBefore(version)) HANDLERS.put(id, handler);
    }
}
