package com.github.zr0n1.multiproto.mixin.parity.block;

import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.zr0n1.multiproto.protocol.ProtocolKt.*;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockTextureMixin extends Block {

    public SaplingBlockTextureMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("deprecation")
    private void applyTextureParity(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if (getCurrVer().isLE(BETA_10)) {
            cir.setReturnValue(textureId);
        }
    }
}
