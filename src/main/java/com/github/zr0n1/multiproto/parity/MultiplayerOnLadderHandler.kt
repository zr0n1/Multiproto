package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.protocol.Protocol
import com.github.zr0n1.multiproto.protocol.Version
import net.minecraft.block.Block
import net.minecraft.client.network.MultiplayerClientPlayerEntity
import net.minecraft.util.math.MathHelper.floor
import net.modificationstation.stationapi.api.entity.player.PlayerHandler

class MultiplayerOnLadderHandler(private val player: MultiplayerClientPlayerEntity) : PlayerHandler {
    override fun isOnLadder(onLadder: Boolean): Boolean = onLadder || (Protocol.version <= Version.B1_4_01 &&
            with(player) { world.getBlockId(floor(x), floor(y) + 1, floor(z)) == Block.LADDER.id })
}
