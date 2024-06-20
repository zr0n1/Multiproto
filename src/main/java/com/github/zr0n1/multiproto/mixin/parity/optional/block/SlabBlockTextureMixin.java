package com.github.zr0n1.multiproto.mixin.parity.optional.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.parity.optional.TextureHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.zr0n1.multiproto.protocol.ProtocolKt.*;

@Mixin(SlabBlock.class)
@SuppressWarnings("deprecation")
public abstract class SlabBlockTextureMixin {

    @Shadow
    public abstract boolean isFullCube();

    @Inject(method = "getTexture(II)I", at = @At("HEAD"), cancellable = true)
    private void multiproto_applyTextureParity(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if (getCurrVer().isLE(BETA_13) && Multiproto.config.textureParity) {
            if (!isFullCube() && side > 1) cir.setReturnValue(TextureHelper.slabSideTextures[meta]);
            else if (meta == 3) cir.setReturnValue(Block.COBBLESTONE.textureId);
        }
    }
}
