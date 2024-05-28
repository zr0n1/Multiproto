package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.api.packet.DataType;
import com.github.zr0n1.multiproto.api.packet.FieldEntry;
import com.github.zr0n1.multiproto.api.packet.PacketWrapper;
import com.github.zr0n1.multiproto.mixin.parity.entity.EntityAccessor;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import net.minecraft.network.packet.play.PlayerRespawnPacket;
import net.minecraft.network.packet.s2c.play.*;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.mixin.entity.client.ClientNetworkHandlerAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PacketTranslator {

    private static final Map<Integer, Function<Packet, PacketWrapper<Packet>>> WRAPPERS = new HashMap<>();
    private static final Map<Integer, Function<Packet, Packet>> C2S_REDIRECTS = new HashMap<>();
    private static final BiMap<Integer, Packet> REPLACEMENTS = HashBiMap.create();

    public static Packet wrap(Packet packet) {
        return packet != null ? WRAPPERS.get(packet.getRawId()).apply(packet) : null;
    }

    public static Packet redirect(Packet packet) {
        return C2S_REDIRECTS.containsKey(packet.getRawId()) ? C2S_REDIRECTS.get(packet.getRawId()).apply(packet) : packet;
    }

    public static Packet replace(int id) {
        return REPLACEMENTS.get(id);
    }

    public static boolean isWrapped(int id) {
        return WRAPPERS.containsKey(id);
    }

    public static boolean isWrapped(Packet packet) {
        return packet instanceof PacketWrapper<?> wrapper && wrapper.holder instanceof Packet;
    }

    public static boolean isRedirected(int id) {
        return C2S_REDIRECTS.containsKey(id);
    }

    public static boolean isReplaced(int id) {
        return REPLACEMENTS.containsKey(id);
    }

    public static boolean isReplaced(Packet packet) {
        return REPLACEMENTS.containsValue(packet);
    }

    public static int getReplacementId(Packet packet) {
        return REPLACEMENTS.inverse().get(packet);
    }

    public static void applyChanges() {
        WRAPPERS.clear();
        C2S_REDIRECTS.clear();
        REPLACEMENTS.clear();
        // < b1.7
        wrapBefore(Version.BETA_14, new PacketWrapper<LoginHelloPacket>(1,
                FieldEntry.of(DataType.INT, p -> VersionManager.getVersion().protocol), // protocol
                FieldEntry.of(DataType.string(16)), // username
                FieldEntry.of(DataType.LONG), // seed
                FieldEntry.of(DataType.BYTE) // dimension
        ).postWrite(PacketTranslator::logLogin));
        // < b1.6
        wrapBefore(Version.BETA_13, new PacketWrapper<EntitySpawnS2CPacket>(23,
                FieldEntry.of(DataType.INT), // id
                FieldEntry.of(DataType.BYTE, 7), // entity type
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.of(DataType.dummy(0)), // velocity x
                FieldEntry.of(DataType.dummy(0)), // velocity y
                FieldEntry.of(DataType.dummy(0)) // velocity z
        ));
        wrapBefore(Version.BETA_13, new PacketWrapper<PlayerRespawnPacket>(9));
        // < b1.5
        wrapBefore(Version.BETA_11, new PacketWrapper<LoginHelloPacket>(1,
                FieldEntry.of(DataType.INT, p -> VersionManager.getVersion().protocol), // protocol
                FieldEntry.of(DataType.UTF), // username
                FieldEntry.unique(DataType.UTF, "Password"), // password
                FieldEntry.of(DataType.LONG), // seed
                FieldEntry.of(DataType.BYTE) // dimension
        ).postWrite(PacketTranslator::logLogin));
        wrapBefore(Version.BETA_11, new PacketWrapper<ClickSlotC2SPacket>(102,
                FieldEntry.of(DataType.BYTE), // id
                FieldEntry.of(DataType.SHORT), // slot
                FieldEntry.of(DataType.BYTE), // button
                FieldEntry.of(DataType.SHORT), // action type
                FieldEntry.of(DataType.dummy(false), 5), // shift click
                FieldEntry.of(DataType.ITEM_STACK) // itemstack
        ));
        // < b1.2
        wrapBefore(Version.BETA_8, new PacketWrapper<EntityEquipmentUpdateS2CPacket>(5,
                FieldEntry.of(DataType.INT), // entity id
                FieldEntry.of(DataType.SHORT), // slot id
                FieldEntry.of(DataType.SHORT), // item id
                FieldEntry.of(DataType.dummy(0)) // damage
        ));
        wrapBefore(Version.BETA_8, new PacketWrapper<PlayerInteractBlockC2SPacket>(15,
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.ITEM_STACK)
        ));
        redirectBefore(Version.BETA_8, 19, (ClientCommandC2SPacket packet) ->
                new EntityAnimationPacket(PlayerHelper.getPlayerFromGame(), packet.mode == 1 ? 104 : 105));
        wrapBefore(Version.BETA_8, new PacketWrapper<EntityAnimationPacket>(18,
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE))
                .apply((packet, handler) -> {
                    Entity e = ((ClientNetworkHandlerAccessor) handler).invokeMethod_1645(packet.id);
                    if(e != null && (packet.animationId == 104 || packet.animationId == 105)) {
                        ((EntityAccessor) e).invokeSetFlag(1, packet.animationId == 104); // start/stop sneaking
                    } else packet.apply(handler);
                }));
        wrapBefore(Version.BETA_8, new PacketWrapper<ItemEntitySpawnS2CPacket>(21,
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
        wrapBefore(Version.BETA_8, new PacketWrapper<LivingEntitySpawnS2CPacket>(24,
                FieldEntry.of(DataType.INT), // id
                FieldEntry.of(DataType.BYTE), // entity type
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.of(DataType.BYTE), // yaw
                FieldEntry.of(DataType.BYTE) // pitch
        ));
        wrapBefore(Version.BETA_8, new PacketWrapper<ScreenHandlerSlotUpdateS2CPacket>(103,
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.SHORT),
                FieldEntry.of(DataType.ITEM_STACK)
        ));
    }

    @SuppressWarnings("unchecked")
    public static void wrapBefore(Version target, PacketWrapper<? extends Packet> wrapper) {
        if (VersionManager.isBefore(target)) WRAPPERS.put(wrapper.id, packet -> ((PacketWrapper<Packet>) wrapper).wrap(packet));
    }

    @SuppressWarnings("unchecked")
    public static void redirectBefore(Version target, int id, Function<? extends Packet, Packet> redirect) {
        if (VersionManager.isBefore(target)) C2S_REDIRECTS.put(id, (Function<Packet, Packet>) redirect);
    }

    public static void replaceBefore(Version target, int id, Packet packet) {
        if (VersionManager.isBefore(target)) REPLACEMENTS.put(id, packet);
    }

    private static void logLogin(LoginHelloPacket hello) {
        Multiproto.LOGGER.info("Logged in as {} on protocol version {}",
                hello.username, hello.protocolVersion);
    }
}
