package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockMixin extends Block {

    public FurnaceBlockMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void textureParity(int side, CallbackInfoReturnable<Integer> cir) {
        if(side == 1 || side == 0 && Multiproto.getVersion().compareTo(ProtocolVersion.BETA_8) < 0 &&
                Multiproto.config.visualParity) {
            cir.setReturnValue(STONE.textureId);
        }
    }

    @Inject(method = "getTextureId", at = @At("HEAD"), cancellable = true)
    private void textureIdParity(BlockView bv, int x, int y, int z, int side, CallbackInfoReturnable<Integer> cir) {
        if(side == 1 || side == 0 && Multiproto.getVersion().compareTo(ProtocolVersion.BETA_8) < 0 &&
                Multiproto.config.visualParity) {
            cir.setReturnValue(STONE.textureId);
        }
    }
}