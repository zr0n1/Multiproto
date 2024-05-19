package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.Utils;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockTextureMixin extends Block {

    public SaplingBlockTextureMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void applyTextureParity(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if(Utils.getVersion().compareTo(ProtocolVersion.BETA_11) < 0) {
            cir.setReturnValue(textureId);
        }
    }
}
