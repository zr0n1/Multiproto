package com.github.zr0n1.multiproto.mixin.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.color.world.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {

    public LeavesBlockMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void getTexture(CallbackInfoReturnable<Integer> cir) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) {
            cir.setReturnValue(textureId);
        }
    }

    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void getColor(CallbackInfoReturnable<Integer> cir) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) {
            cir.setReturnValue(FoliageColors.getDefaultColor());
        }
    }

    @Inject(method = "getColorMultiplier", at = @At("HEAD"), cancellable = true)
    private void getColorMultiplier(CallbackInfoReturnable<Integer> cir) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) {
            cir.setReturnValue(FoliageColors.getDefaultColor());
        }
    }
}
