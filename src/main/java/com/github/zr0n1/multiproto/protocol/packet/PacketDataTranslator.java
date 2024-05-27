package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.api.packet.DataType;
import com.github.zr0n1.multiproto.api.packet.FieldEntry;
import com.github.zr0n1.multiproto.api.packet.PacketDataHandler;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PacketDataTranslator {

    private static final Map<Integer, PacketDataHandler<? extends Packet>> HANDLERS = new HashMap<>();

    public static void read(Packet packet, DataInputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getRawId())) {
            HANDLERS.get(packet.getRawId()).read(packet, stream);
        } else packet.read(stream);
    }

    public static void write(Packet packet, DataOutputStream stream) throws IOException {
        if (HANDLERS.containsKey(packet.getRawId())) {
            HANDLERS.get(packet.getRawId()).write(packet, stream);
        } else packet.write(stream);
    }

    public static int size(Packet packet) {
        return HANDLERS.containsKey(packet.getRawId()) ? HANDLERS.get(packet.getRawId()).size(packet) : packet.size();
    }

    public static void apply(Packet packet, NetworkHandler handler) {
        if (HANDLERS.containsKey(packet.getRawId())) {
            HANDLERS.get(packet.getRawId()).apply(packet, handler);
        } else packet.apply(handler);
    }

    public static void applyChanges() {
        before(Version.BETA_14, 1, new PacketDataHandler<LoginHelloPacket>(
                FieldEntry.unique(DataType.INT, VersionManager.getVersion().protocol),
                FieldEntry.of(DataType.string(16)),
                FieldEntry.of(DataType.LONG),
                FieldEntry.of(DataType.BYTE)
        ));
        before(Version.BETA_13, 23, new PacketDataHandler<EntitySpawnS2CPacket>(
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE, 7),
                FieldEntry.of(DataType.INT, 1),
                FieldEntry.of(DataType.INT, 2),
                FieldEntry.of(DataType.INT, 3)
        ));
        before(Version.BETA_13, 9, new PacketDataHandler<PlayerRespawnPacket>());
        before(Version.BETA_11, 1, new PacketDataHandler<LoginHelloPacket>(
                FieldEntry.unique(DataType.INT, VersionManager.getVersion().protocol),
                FieldEntry.of(DataType.UTF),
                FieldEntry.unique(DataType.UTF, "Password"),
                FieldEntry.of(DataType.LONG),
                FieldEntry.of(DataType.BYTE)
        ));
    }

    public static void before(Version target, int id, PacketDataHandler<? extends Packet> handler) {
        if (VersionManager.isBefore(target)) HANDLERS.put(id, handler);
    }
}
