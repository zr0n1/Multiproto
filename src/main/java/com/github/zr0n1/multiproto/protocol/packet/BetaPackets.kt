package com.github.zr0n1.multiproto.protocol.packet

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.api.packet.DataType
import com.github.zr0n1.multiproto.api.packet.FieldEntry
import com.github.zr0n1.multiproto.api.packet.PacketWrapper
import com.github.zr0n1.multiproto.protocol.*
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator.wrapLE
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
import net.minecraft.network.packet.login.LoginHelloPacket
import net.minecraft.network.packet.play.PlayerRespawnPacket
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket

internal object BetaPackets : VersionChangedListener {
    override fun invoke() {
        // <= b1.6.6
        wrapLE(BETA_13, 1) { packet: LoginHelloPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.INT, writeValue = { currVer.protocol }), // protocol
                FieldEntry(DataType.string(16)), // username
                if (currVer <= BETA_10) FieldEntry.unique(DataType.UTF, "Password") else null, // password
                if (currVer >= ALPHA_3) FieldEntry(DataType.LONG) else FieldEntry.dummy(0L), // seed
                if (currVer >= ALPHA_3) FieldEntry(DataType.BYTE) else FieldEntry.dummy(0), // dimension
                postWrite = { Multiproto.LOGGER.info("Logging in with protocol version ${it.protocolVersion}") }
            )
        }
        // <= b1.5_01
        wrapLE(BETA_11, 23) { packet: EntitySpawnS2CPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.INT), // id
                FieldEntry(DataType.BYTE, 7), // entity type
                FieldEntry(DataType.INT), // x
                FieldEntry(DataType.INT), // y
                FieldEntry(DataType.INT), // z
                FieldEntry.dummy(0), // velocity x
                FieldEntry.dummy(0), // velocity y
                FieldEntry.dummy(0) // velocity z
            )
        }
        wrapLE(BETA_11, 9) { packet: PlayerRespawnPacket -> PacketWrapper(packet) } // no values
        // <= b1.4_01
        wrapLE(BETA_10, 102) { packet: ClickSlotC2SPacket ->
            PacketWrapper(
                packet,
                FieldEntry(DataType.BYTE), // id
                FieldEntry(DataType.SHORT), // slot
                FieldEntry(DataType.BYTE), // button
                FieldEntry(DataType.SHORT), // action type
                FieldEntry.dummy(false, 5), // shift click
                FieldEntry(DataType.ITEM_STACK), // itemstack
            )
        }
    }
}
