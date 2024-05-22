package com.github.zr0n1.multiproto.mixin.parity.optional.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.parity.optional.TextureParityHelper;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlabBlock.class)
public abstract class SlabBlockTextureMixin {

    @Shadow
    public abstract boolean isFullCube();

    @Inject(method = "getTexture(II)I", at = @At("HEAD"), cancellable = true)
    private void applyTextureParity(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_14) && Multiproto.config.textureParity) {
            if (!isFullCube() && side > 1) cir.setReturnValue(TextureParityHelper.slabSideTextures[meta]);
            else if (meta == 3) cir.setReturnValue(Block.COBBLESTONE.textureId);
        }
    }
}