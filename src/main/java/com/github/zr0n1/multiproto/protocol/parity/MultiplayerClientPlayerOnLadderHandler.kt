package com.github.zr0n1.multiproto.protocol.parity

import com.github.zr0n1.multiproto.protocol.Protocol
import com.github.zr0n1.multiproto.protocol.Version
import net.minecraft.block.Block
import net.minecraft.client.network.MultiplayerClientPlayerEntity
import net.minecraft.util.math.MathHelper.floor
import net.modificationstation.stationapi.api.entity.player.PlayerHandler

class MultiplayerClientPlayerOnLadderHandler(private val player: MultiplayerClientPlayerEntity) : PlayerHandler {
    override fun isOnLadder(onLadder: Boolean): Boolean = onLadder || (Protocol.version <= Version.B1_4_01 &&
            player.world.getBlockId(floor(player.x), floor(player.y) + 1, floor(player.z)) == Block.LADDER.id)
}
