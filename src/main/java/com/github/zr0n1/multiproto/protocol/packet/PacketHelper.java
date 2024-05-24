package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.github.zr0n1.multiproto.protocol.event.RegisterPacketChangesListener;
import com.github.zr0n1.multiproto.protocol.packet.c2s.play.ClickSlotC2SPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.c2s.play.PlayerInteractBlockC2SPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.login.LoginHelloPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.s2c.play.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.network.packet.s2c.play.*;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class PacketHelper {

    public static Map<Class<? extends Packet>, PacketHandler<? extends Packet>> HANDLERS;
    public static Map<Class<? extends Packet>, Function<Packet, Packet>> REDIRECTS;

    public static void read(Packet packet, DataInputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getClass())) {
            HANDLERS.get(packet.getClass()).readPacket(packet, stream);
        } else packet.read(stream);
    }

    public static void write(Packet packet, DataOutputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getClass())) {
            HANDLERS.get(packet.getClass()).writePacket(packet, stream);
        } else packet.write(stream);
    }

    public static int size(Packet packet) {
        return HANDLERS.containsKey(packet.getClass()) ? HANDLERS.get(packet.getClass()).packetSize(packet) : packet.size();
    }

    public static Packet redirect(Packet packet) {
        return REDIRECTS.containsKey(packet.getClass()) ? REDIRECTS.get(packet.getClass()).apply(packet) : packet;
    }

    public static void register() {
        HANDLERS = new HashMap<>();
        REDIRECTS = new HashMap<>();
        // < b1.7
        registerBefore(Version.BETA_14, LoginHelloPacket.class, new LoginHelloPacketHandler());
        // < b1.6
        registerBefore(Version.BETA_13, EntitySpawnS2CPacket.class, new EntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_13, PlayerRespawnPacket.class, new EmptyPacketHandler<PlayerRespawnPacket>());
        // < b1.5
        registerBefore(Version.BETA_11, ClickSlotC2SPacket.class, new ClickSlotC2SPacketHandler());
        // < b1.2
        registerBefore(Version.BETA_8, EntityEquipmentUpdateS2CPacket.class, new EntityEquipmentUpdateS2CPacketHandler());
        registerBefore(Version.BETA_8, ItemEntitySpawnS2CPacket.class, new ItemEntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_8, LivingEntitySpawnS2CPacket.class, new LivingEntitySpawnS2CPacketHandler());
        registerBefore(Version.BETA_8, PlayerInteractBlockC2SPacket.class, new PlayerInteractBlockC2SPacketHandler());
        registerBefore(Version.BETA_8, ScreenHandlerSlotUpdateS2CPacket.class, new ScreenHandlerSlotUpdateS2CPacketHandler());
        registerBefore(Version.BETA_8, ClientCommandC2SPacket.class, (ClientCommandC2SPacket packet) ->
                new EntityAnimationPacket(PlayerHelper.getPlayerFromGame(), packet.mode == 1 ? 104 : 105));
        // custom packet handlers
        FabricLoader.getInstance().getEntrypointContainers("multiproto:register_packet_changes", RegisterPacketChangesListener.class)
                .forEach(listener -> listener.getEntrypoint().registerHandlers());
        HANDLERS = Collections.unmodifiableMap(HANDLERS);
        REDIRECTS = Collections.unmodifiableMap(REDIRECTS);
    }

    public static void registerBefore(Version target, Class<? extends Packet> plass, PacketHandler<? extends Packet> handler) {
        if (VersionManager.isBefore(target)) HANDLERS.put(plass, handler);
    }

    public static void registerBefore(Version target, Class<? extends Packet> plass, Function<? extends Packet, Packet> redirect) {
        if (VersionManager.isBefore(target)) REDIRECTS.put(plass, (Function<Packet, Packet>) redirect);
    }
}
