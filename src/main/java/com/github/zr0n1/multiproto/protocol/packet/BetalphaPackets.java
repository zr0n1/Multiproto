package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.api.packet.DataType;
import com.github.zr0n1.multiproto.api.packet.FieldEntry;
import com.github.zr0n1.multiproto.api.packet.PacketWrapper;
import com.github.zr0n1.multiproto.mixin.entity.EntityAccessor;
import com.github.zr0n1.multiproto.protocol.Version;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ItemEntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.LivingEntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.mixin.entity.client.ClientNetworkHandlerAccessor;

import static com.github.zr0n1.multiproto.protocol.packet.PacketTranslator.*;

public class BetalphaPackets {
    public static void applyChanges() {
        // <= b1.1
        wrapLE(Version.BETALPHA_8, new PacketWrapper<EntityEquipmentUpdateS2CPacket>(5,
                FieldEntry.of(DataType.INT), // entity id
                FieldEntry.of(DataType.SHORT), // slot id
                FieldEntry.of(DataType.SHORT), // item id
                FieldEntry.dummy(0) // damage
        ));
        wrapLE(Version.BETALPHA_8, new PacketWrapper<PlayerInteractBlockC2SPacket>(15,
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.INT),
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.ITEM_STACK)
        ));
        redirectLE(Version.BETALPHA_8, 19, (ClientCommandC2SPacket packet) ->
                new EntityAnimationPacket(PlayerHelper.getPlayerFromGame(), packet.mode == 1 ? 104 : 105));
        applyLE(Version.BETALPHA_8, 18, (EntityAnimationPacket packet, NetworkHandler handler) -> {
            Entity e = ((ClientNetworkHandlerAccessor) handler).invokeMethod_1645(packet.id);
            if (e != null && (packet.animationId == 104 || packet.animationId == 105)) {
                ((EntityAccessor) e).invokeSetFlag(1, packet.animationId == 104); // start/stop sneaking
            } else packet.apply(handler);
        });
        wrapLE(Version.BETALPHA_8, new PacketWrapper<ItemEntitySpawnS2CPacket>(21,
                FieldEntry.of(DataType.INT), // entity id
                FieldEntry.of(DataType.SHORT, 7), // item id
                FieldEntry.of(DataType.BYTE, 8), // count
                FieldEntry.dummy(0, 9), // damage
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.of(DataType.BYTE), // velocity x
                FieldEntry.of(DataType.BYTE), // velocity y
                FieldEntry.of(DataType.BYTE) // velocity z
        ));
        wrapLE(Version.BETALPHA_8, new PacketWrapper<LivingEntitySpawnS2CPacket>(24,
                FieldEntry.of(DataType.INT), // id
                FieldEntry.of(DataType.BYTE), // entity type
                FieldEntry.of(DataType.INT), // x
                FieldEntry.of(DataType.INT), // y
                FieldEntry.of(DataType.INT), // z
                FieldEntry.of(DataType.BYTE), // yaw
                FieldEntry.of(DataType.BYTE) // pitch
        ));
        wrapLE(Version.BETALPHA_8, new PacketWrapper<ScreenHandlerSlotUpdateS2CPacket>(103,
                FieldEntry.of(DataType.BYTE),
                FieldEntry.of(DataType.SHORT),
                FieldEntry.of(DataType.ITEM_STACK)
        ));
    }
}
