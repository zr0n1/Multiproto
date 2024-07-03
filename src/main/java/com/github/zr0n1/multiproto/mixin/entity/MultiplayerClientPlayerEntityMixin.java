package com.github.zr0n1.multiproto.mixin.entity;

import com.github.zr0n1.multiproto.protocol.Protocol;
import com.github.zr0n1.multiproto.protocol.Version;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerClientPlayerEntity.class)
public abstract class MultiplayerClientPlayerEntityMixin extends PlayerEntity {

    public MultiplayerClientPlayerEntityMixin(World arg) {
        super(arg);
    }

//    @Inject(method = "method_1923", at = @At("HEAD"))
//    private void multiproto_updateInventory(CallbackInfo ci) {
//        if (Protocol.verLE(Version.A1_2_6) && ) {
//
//        }
//    }
}
