package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.api.EmptyPacketHandler;
import com.github.zr0n1.multiproto.api.PacketHandler;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.github.zr0n1.multiproto.protocol.packet.c2s.play.ClickSlotC2SPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.c2s.play.PlayerInteractBlockC2SPacketHandler;
import com.github.zr0n1.multiproto.protocol.packet.s2c.play.*;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class PacketTranslator {

    public static Map<Integer, PacketHandler<? extends Packet>> HANDLERS;
    public static Map<Integer, Function<Packet, Packet>> REDIRECTS;

    public static void read(Packet packet, DataInputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getRawId())) {
            HANDLERS.get(packet.getRawId()).readPacket(packet, stream);
        } else packet.read(stream);
    }

    public static void write(Packet packet, DataOutputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getRawId())) {
            HANDLERS.get(packet.getRawId()).writePacket(packet, stream);
        } else packet.write(stream);
    }

    public static int size(Packet packet) {
        return HANDLERS.containsKey(packet.getRawId()) ? HANDLERS.get(packet.getRawId()).packetSize(packet) : packet.size();
    }

    public static Packet redirect(Packet packet) {
        return REDIRECTS.containsKey(packet.getRawId()) ? REDIRECTS.get(packet.getRawId()).apply(packet) : packet;
    }

    public static void applyChanges() {
        HANDLERS = new HashMap<>();
        REDIRECTS = new HashMap<>();
        // < b1.7
        //before(Version.BETA_14, 1, new LoginHelloPacketHandler());
        // < b1.6
        before(Version.BETA_13, 23, new EntitySpawnS2CPacketHandler());
        before(Version.BETA_13, 9, new EmptyPacketHandler<PlayerRespawnPacket>());
        // < b1.5
        before(Version.BETA_11, 102, new ClickSlotC2SPacketHandler());
        // < b1.2
        before(Version.BETA_8, 5, new EntityEquipmentUpdateS2CPacketHandler());
        before(Version.BETA_8, 15, new PlayerInteractBlockC2SPacketHandler());
        before(Version.BETA_8, 19, (ClientCommandC2SPacket packet) ->
                new EntityAnimationPacket(PlayerHelper.getPlayerFromGame(), packet.mode == 1 ? 104 : 105));
        before(Version.BETA_8, 21, new ItemEntitySpawnS2CPacketHandler());
        before(Version.BETA_8, 24, new LivingEntitySpawnS2CPacketHandler());
        before(Version.BETA_8, 103, new ScreenHandlerSlotUpdateS2CPacketHandler());
        HANDLERS = Collections.unmodifiableMap(HANDLERS);
        REDIRECTS = Collections.unmodifiableMap(REDIRECTS);
    }

    public static void before(Version target, int id, PacketHandler<? extends Packet> handler) {
        if (VersionManager.isBefore(target)) HANDLERS.put(id, handler);
    }

    public static void before(Version target, int id, Function<? extends Packet, Packet> redirect) {
        if (VersionManager.isBefore(target)) REDIRECTS.put(id, (Function<Packet, Packet>) redirect);
    }

    public static void onOrAfter(Version target, int id, PacketHandler<? extends Packet> handler) {
        if (!VersionManager.isBefore(target)) HANDLERS.put(id, handler);
    }

    public static void onOrAfter(Version target, int id, Function<? extends Packet, Packet> redirect) {
        if (!VersionManager.isBefore(target)) REDIRECTS.put(id, (Function<Packet, Packet>) redirect);
    }

    public static void only(Version target, int id, PacketHandler<? extends Packet> handler) {
        if (VersionManager.getVersion() == target) HANDLERS.put(id, handler);
    }

    public static void only(Version target, int id, Function<? extends Packet, Packet> redirect) {
        if (VersionManager.getVersion() == target) REDIRECTS.put(id, (Function<Packet, Packet>) redirect);
    }
}
