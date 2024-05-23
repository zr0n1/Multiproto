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

    public static final Map<Class<? extends Packet>, PacketHandler<? extends Packet>> HANDLERS = new HashMap<>();

    public static void read(Packet packet, DataInputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getClass())) HANDLERS.get(packet.getClass()).readPacket(packet, stream);
        else packet.read(stream);
    }

    public static void write(Packet packet, DataOutputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getClass())) HANDLERS.get(packet.getClass()).writePacket(packet, stream);
        else packet.write(stream);
    }

    public static int size(Packet packet) {
        return (HANDLERS.containsKey(packet.getClass())) ? HANDLERS.get(packet.getClass()).packetSize(packet) : packet.size();
    }

    public static void registerHandlers() {
        registerBefore(Version.BETA_14, LoginHelloPacket.class, new LoginHelloPacketHandler());
        registerBefore(Version.BETA_13, EntitySpawnS2CPacket.class, new EntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_13, PlayerRespawnPacket.class, new EmptyPacketHandler());
        registerBefore(Version.BETA_11, ClickSlotC2SPacket.class, new ClickSlotC2SPacketHandler());
        registerBefore(Version.BETA_8, EntityEquipmentUpdateS2CPacket.class, new EntityEquipmentUpdateS2CPacketHandler());
        registerBefore(Version.BETA_8, ItemEntitySpawnS2CPacket.class, new ItemEntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_8, LivingEntitySpawnS2CPacket.class, new LivingEntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_8, PlayerInteractBlockC2SPacket.class, new PlayerInteractBlockC2SPacketHandler());
        registerBefore(Version.BETA_8, ScreenHandlerSlotUpdateS2CPacket.class, new ScreenHandlerSlotUpdateS2CPacketHandler());
        FabricLoader.getInstance().getEntrypointContainers("multiproto:register_versions", RegisterPacketHandlersListener.class)
                .forEach(listener -> listener.getEntrypoint().registerHandlers());
    }

    public static void registerBefore(Version version, Class<? extends Packet> plass, PacketHandler<? extends Packet> handler) {
        if (VersionManager.isBefore(version)) HANDLERS.put(plass, handler);
    }
}
