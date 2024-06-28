package com.github.zr0n1.multiproto.mixin.parity.optional.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.Protocol;
import com.github.zr0n1.multiproto.protocol.Version;
import net.minecraft.block.Block;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceBlock.class)
public abstract class FurnaceBlockTextureMixin {

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("deprecation")
    private void multiproto_applyTextureParity(int side, CallbackInfoReturnable<Integer> cir) {
        if ((side == 1 || side == 0) && Protocol.getVer().isLE(Version.B1_1_02) && Multiproto.config.textureParity) {
            cir.setReturnValue(Block.STONE.textureId);
        }
    }

    @Inject(method = "getTextureId", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("deprecation")
    private void multiproto_applyTextureIdParity(BlockView bv, int x, int y, int z, int side,
                                                 CallbackInfoReturnable<Integer> cir) {
        if ((side == 1 || side == 0) && Protocol.getVer().isLE(Version.B1_1_02) && Multiproto.config.textureParity) {
            cir.setReturnValue(Block.STONE.textureId);
        }
    }
}
