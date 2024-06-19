package com.github.zr0n1.multiproto.protocol.packet

import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.api.packet.DataType
import com.github.zr0n1.multiproto.api.packet.FieldEntry
import com.github.zr0n1.multiproto.api.packet.PacketWrapper
import com.github.zr0n1.multiproto.mixin.entity.EntityAccessor
import com.github.zr0n1.multiproto.protocol.*
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator.applyLE
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator.redirectLE
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator.wrapLE
import net.minecraft.network.NetworkHandler
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
import net.minecraft.network.packet.play.EntityAnimationPacket
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.ItemEntitySpawnS2CPacket
import net.minecraft.network.packet.s2c.play.LivingEntitySpawnS2CPacket
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket
import net.modificationstation.stationapi.api.entity.player.PlayerHelper
import net.modificationstation.stationapi.mixin.entity.client.ClientNetworkHandlerAccessor

internal object BetalphaPackets : VersionChangedListener {
    override fun invoke() {
        // <= b1.1
        wrapLE(BETALPHA_8, 5) { packet: EntityEquipmentUpdateS2CPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.INT), // entity id
                FieldEntry(DataType.SHORT), // slot id
                FieldEntry(DataType.SHORT), // item id
                FieldEntry.dummy(0) // damage
            )
        }
        wrapLE(BETALPHA_8, 15) { packet: PlayerInteractBlockC2SPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.INT),
                FieldEntry(DataType.BYTE),
                FieldEntry(DataType.INT),
                FieldEntry(DataType.BYTE),
                FieldEntry(DataType.ITEM_STACK)
            )
        }
        redirectLE(BETALPHA_8, 19) { packet: ClientCommandC2SPacket ->
            EntityAnimationPacket(PlayerHelper.getPlayerFromGame(), if (packet.mode == 1) 104 else 105)
        }
        applyLE(BETALPHA_8, 18) { packet: EntityAnimationPacket, handler: NetworkHandler ->
            val e = (handler as ClientNetworkHandlerAccessor).invokeMethod_1645(packet.id)
            if (e != null && (packet.animationId == 104 || packet.animationId == 105)) {
                (e as EntityAccessor).invokeSetFlag(1, packet.animationId == 104) // start/stop sneaking
            } else packet.apply(handler)
        }
        wrapLE(BETALPHA_8, 21) { packet: ItemEntitySpawnS2CPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.INT), // entity id
                FieldEntry(DataType.SHORT, 7), // item id
                FieldEntry(DataType.BYTE, 8), // count
                FieldEntry.dummy(0, 9), // damage
                FieldEntry(DataType.INT), // x
                FieldEntry(DataType.INT), // y
                FieldEntry(DataType.INT), // z
                FieldEntry(DataType.BYTE), // velocity x
                FieldEntry(DataType.BYTE), // velocity y
                FieldEntry(DataType.BYTE) // velocity z
            )
        }
        wrapLE(BETALPHA_8, 24) { packet: LivingEntitySpawnS2CPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.INT), // id
                FieldEntry(DataType.BYTE), // entity type
                FieldEntry(DataType.INT), // x
                FieldEntry(DataType.INT), // y
                FieldEntry(DataType.INT), // z
                FieldEntry(DataType.BYTE), // yaw
                FieldEntry(DataType.BYTE) // pitch
            )
        }
        wrapLE(BETALPHA_8, 103) { packet: ScreenHandlerSlotUpdateS2CPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.BYTE),
                FieldEntry(DataType.SHORT),
                FieldEntry(DataType.ITEM_STACK)
            )
        }
    }
}
