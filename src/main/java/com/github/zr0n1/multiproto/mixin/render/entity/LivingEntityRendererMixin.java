package com.github.zr0n1.multiproto.mixin.render.entity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer {

    @ModifyVariable(method = "method_818(Lnet/minecraft/entity/LivingEntity;Ljava/lang/String;DDDI)V", at = @At("STORE"), ordinal = 1)
    private float modifyNameScale(float scale, @Local(ordinal = 0) float distance, @Local(argsOnly = true)LivingEntity e) {
        return (Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) < 0 &&
                Multiproto.config.versionGraphics && e instanceof PlayerEntity) ?
                (float)((double)scale * (Math.sqrt(distance) / 2.0D)) : scale;
    }
}
