package com.github.zr0n1.multiproto.mixin.parity.optional.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.parity.optional.TextureHelper;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.zr0n1.multiproto.protocol.ProtocolKt.*;

@Mixin(RedstoneWireBlock.class)
@SuppressWarnings("deprecation")
public abstract class RedstoneWireBlockTextureMixin extends Block {

    public RedstoneWireBlockTextureMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void multiproto_applyTextureParity(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if (getCurrVer().isLE(BETA_8) && Multiproto.config.textureParity) {
            cir.setReturnValue(meta > 0 ? TextureHelper.redstoneWireTextures[1] : TextureHelper.redstoneWireTextures[0]);
        }
    }

    @Inject(method = "getColorMultiplier", at = @At("HEAD"), cancellable = true)
    private void multiproto_applyTextureColorParity(BlockView blockView, int x, int y, int z,
                                                    CallbackInfoReturnable<Integer> cir) {
        if (getCurrVer().isLE(BETA_10) && Multiproto.config.textureParity) {
            cir.setReturnValue(super.getColorMultiplier(blockView, x, y, z));
        }
    }
}
