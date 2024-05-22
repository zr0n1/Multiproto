package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.block.Block;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;

public class MultiplayerClientPlayerOnLadderHandler implements PlayerHandler {

    private final PlayerEntity player;

    public MultiplayerClientPlayerOnLadderHandler(MultiplayerClientPlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean isOnLadder(boolean onLadder) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_11)) {
            int x = MathHelper.floor(player.x);
            int y = MathHelper.floor(player.boundingBox.minY);
            int z = MathHelper.floor(player.z);
            return onLadder || player.world.getBlockId(x, y + 1, z) == Block.LADDER.id;
        }
        return onLadder;
    }
}
