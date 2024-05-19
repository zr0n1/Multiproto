package com.github.zr0n1.multiproto.mixin.parity.entity;

import com.github.zr0n1.multiproto.Utils;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLadderMixin extends Entity {

    public LivingEntityLadderMixin(World world) {
        super(world);
    }

    @Inject(method = "method_932", at = @At("TAIL"), cancellable = true)
    private void applyLadderParity(CallbackInfoReturnable<Boolean> cir,
                                   @Local(ordinal = 0) int x, @Local(ordinal = 1) int y, @Local(ordinal = 2) int z) {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_11) < 0) {
            cir.setReturnValue(cir.getReturnValue() || this.world.getBlockId(x, y + 1, z) == Block.LADDER.id);
        }
    }
}
