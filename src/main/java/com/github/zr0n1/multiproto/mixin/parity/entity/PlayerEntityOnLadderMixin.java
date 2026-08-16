package com.github.zr0n1.multiproto.mixin.parity.entity;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MultiplayerClientPlayerEntity.class)
public class PlayerEntityOnLadderMixin extends LivingEntityMixin  {

    public boolean multiproto_isOnLadder(Operation<Boolean> original) {
        return original.call() || multiproto_IsLadderOnTop();
    }

    @Unique
    private boolean multiproto_IsLadderOnTop() {
        if(!(ProtocolVersionManager.getVersion().isBukkitClient() || ProtocolVersionManager.isBefore(ProtocolVersion.BETA_11))) return false;
        MultiplayerClientPlayerEntity player = (MultiplayerClientPlayerEntity)(Object)this;
        int x = MathHelper.floor(player.x);
        int y = MathHelper.floor(player.boundingBox.minY);
        int z = MathHelper.floor(player.z);
        return player.world.getBlockId(x, y + 1, z) == Block.LADDER.id;
    }

}
