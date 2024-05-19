package com.github.zr0n1.multiproto.mixin.parity.render.entity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.Utils;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityNameRendererMixin extends LivingEntityRenderer {

    public PlayerEntityNameRendererMixin(EntityModel entityModel, float shadowRadius) {
        super(entityModel, shadowRadius);
    }

    @ModifyVariable(method = "method_821(Lnet/minecraft/entity/player/PlayerEntity;DDD)V", at = @At("STORE"), ordinal = 1)
    private float applyNameScaleParity(float f, @Local(argsOnly = true) PlayerEntity e) {
        return (Utils.getVersion().compareTo(ProtocolVersion.BETA_9) < 0 && Multiproto.config.nameScaleParity) ?
                (float)((double)f * (Math.sqrt(e.getDistance(dispatcher.field_2496)) / 2.0D)) : f;
    }
}