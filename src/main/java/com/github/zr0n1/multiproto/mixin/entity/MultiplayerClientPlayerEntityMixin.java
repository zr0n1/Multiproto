package com.github.zr0n1.multiproto.mixin.entity;

import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiplayerClientPlayerEntity.class)
public abstract class MultiplayerClientPlayerEntityMixin extends PlayerEntity {

    public MultiplayerClientPlayerEntityMixin(World arg) {
        super(arg);
    }

//    @Inject(method = "method_1923", at = @At("HEAD"))
//    private void updateInventory(CallbackInfo ci) {
//        if (VersionManager.isLE(Version.ALPHAWEEN_6) && ) {
//
//        }
//    }
}
