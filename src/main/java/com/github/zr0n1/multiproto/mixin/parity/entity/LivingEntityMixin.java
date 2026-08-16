package com.github.zr0n1.multiproto.mixin.parity.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @WrapMethod(method="isOnLadder")
    public boolean multiproto_isOnLadder(Operation<Boolean> original) {
        return original.call();
    }
}
