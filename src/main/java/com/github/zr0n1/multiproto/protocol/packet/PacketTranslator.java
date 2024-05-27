package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.api.packet.DataType;
import com.github.zr0n1.multiproto.api.packet.FieldEntry;
import com.github.zr0n1.multiproto.api.packet.PacketData;
import com.github.zr0n1.multiproto.mixin.parity.entity.EntityAccessor;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.network.packet.s2c.play.*;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.mixin.entity.client.ClientNetworkHandlerAccessor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PacketTranslator {

    private static final Map<Integer, PacketData<? extends Packet>> HANDLERS = new HashMap<>();
    public static final Map<Integer, Function<Packet, Packet>> C2S_REDIRECTS = new HashMap<>();

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

    public static Packet redirect(Packet packet) {
        return C2S_REDIRECTS.containsKey(packet.getRawId()) ? C2S_REDIRECTS.get(packet.getRawId()).apply(packet) : packet;
    }

    public static void applyChanges() {
        HANDLERS.clear();
        C2S_REDIRECTS.clear();
        // < b1.7
        before(Version.BETA_14, 1, new PacketData<LoginHelloPacket>(
                FieldEntry.of(DataType.INT, p -> VersionManager.getVersion().protocol), // protocol
                FieldEntry.of(DataType.string(16)), // username
                FieldEntry.of(DataType.LONG), // seed
                FieldEntry.of(DataType.BYTE) // dimension
        ).postWrite(PacketTranslator::logLogin));
        // < b1.6
        before(Version.BETA_13, 23, new PacketData<EntitySpawnS2CPacket>(
                FieldEntry.of(DataType.INT), // id
                FieldEntry.of(DataType.BYTE, 7), // entity type
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.of(DataType.dummy(0)), // velocity x
                FieldEntry.of(DataType.dummy(0)), // velocity y
                FieldEntry.of(DataType.dummy(0)) // velocity z
        ));
        before(Version.BETA_13, 9, new PacketData<PlayerRespawnPacket>());
        // < b1.5
        before(Version.BETA_11, 1, new PacketData<LoginHelloPacket>(
                FieldEntry.of(DataType.INT, p -> VersionManager.getVersion().protocol), // protocol
                FieldEntry.of(DataType.UTF), // username
                FieldEntry.unique(DataType.UTF, "Password"), // password
                FieldEntry.of(DataType.LONG), // seed
                FieldEntry.of(DataType.BYTE) // dimension
        ).postWrite(PacketTranslator::logLogin));
        before(Version.BETA_11, 102, new PacketData<ClickSlotC2SPacket>(
                FieldEntry.of(DataType.BYTE), // id
                FieldEntry.of(DataType.SHORT), // slot
                FieldEntry.of(DataType.BYTE), // button
                FieldEntry.of(DataType.SHORT), // action type
                FieldEntry.of(DataType.dummy(false), 5), // shift click
                FieldEntry.of(DataType.ITEM_STACK) // itemstack
        ));
        // < b1.2
        before(Version.BETA_8, 5, new PacketData<EntityEquipmentUpdateS2CPacket>(
                FieldEntry.of(DataType.INT), // entity id
                FieldEntry.of(DataType.SHORT), // slot id
                FieldEntry.of(DataType.SHORT), // item id
                FieldEntry.of(DataType.dummy(0)) // damage
        ));
        before(Version.BETA_8, 15, new PacketData<>(
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.ITEM_STACK)
        ));
        beforeC2S(Version.BETA_8, 19, (ClientCommandC2SPacket packet) ->
                new EntityAnimationPacket(PlayerHelper.getPlayerFromGame(), packet.mode == 1 ? 104 : 105));
        before(Version.BETA_8, 18, new PacketData<EntityAnimationPacket>(FieldEntry.of(DataType.INT), FieldEntry.of(DataType.BYTE))
                .apply((packet, handler) -> {
                    Entity e = ((ClientNetworkHandlerAccessor) handler).invokeMethod_1645(packet.id);
                    if (e != null && packet.animationId == 104) ((EntityAccessor) e).invokeSetFlag(1, true);
                    if (e != null && packet.animationId == 105) ((EntityAccessor) e).invokeSetFlag(1, false);
                    packet.apply(handler);
                }));
        before(Version.BETA_8, 21, new PacketData<ItemEntitySpawnS2CPacket>(
                FieldEntry.of(DataType.INT), // entity id
                FieldEntry.of(DataType.SHORT, 7), // item id
                FieldEntry.of(DataType.BYTE, 8), // count
                FieldEntry.of(DataType.dummy(0), 9), // damage
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.of(DataType.BYTE), // velocity x
                FieldEntry.of(DataType.BYTE), // velocity y
                FieldEntry.of(DataType.BYTE) // velocity z
        ));
        before(Version.BETA_8, 24, new PacketData<LivingEntitySpawnS2CPacket>(
                FieldEntry.of(DataType.INT), // id
                FieldEntry.of(DataType.BYTE), // entity type
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.of(DataType.BYTE), // yaw
                FieldEntry.of(DataType.BYTE) // pitch
        ));
        before(Version.BETA_8, 103, new PacketData<ScreenHandlerSlotUpdateS2CPacket>(
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.SHORT),
                FieldEntry.of(DataType.ITEM_STACK)
        ));
    }

    public static void before(Version target, int id, PacketData<? extends Packet> handler) {
        if (VersionManager.isBefore(target)) HANDLERS.put(id, handler);
    }

    @SuppressWarnings("unchecked")
    public static void beforeC2S(Version target, int id, Function<? extends Packet, Packet> redirect) {
        if (VersionManager.isBefore(target)) C2S_REDIRECTS.put(id, (Function<Packet, Packet>) redirect);
    }

    private static void logLogin(LoginHelloPacket hello) {
        Multiproto.LOGGER.info("Logged in as {} on protocol version {}",
                hello.username, hello.protocolVersion);
    }
}
