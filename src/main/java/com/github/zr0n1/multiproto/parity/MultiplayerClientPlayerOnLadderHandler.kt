package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.protocol.BETA_10
import com.github.zr0n1.multiproto.protocol.currVer
import net.minecraft.block.Block
import net.minecraft.client.network.MultiplayerClientPlayerEntity
import net.minecraft.util.math.MathHelper.floor
import net.modificationstation.stationapi.api.entity.player.PlayerHandler

class MultiplayerClientPlayerOnLadderHandler(private val player: MultiplayerClientPlayerEntity) : PlayerHandler {
    override fun isOnLadder(onLadder: Boolean): Boolean = onLadder || (currVer <= BETA_10 &&
            player.world.getBlockId(floor(player.x), floor(player.y) + 1, floor(player.z)) == Block.LADDER.id)
}
