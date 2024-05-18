package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.block.SlabBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlabBlock.class)
public abstract class SlabBlockMixin {

    @Shadow
    public abstract boolean isFullCube();

    @Inject(method = "getTexture(II)I", at = @At("HEAD"), cancellable = true)
    private void textureParity(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_14) < 0 &&
                Multiproto.config.visualParity) {
            if(!isFullCube() && meta == 0 && side > 1) cir.setReturnValue(BlockParityHelper.stoneSlabSideTexture);
            if(!isFullCube() && meta == 1 && side > 1) cir.setReturnValue(BlockParityHelper.sandstoneSlabSideTexture);
            if(!isFullCube() && meta == 2 && side > 1) cir.setReturnValue(BlockParityHelper.planksSlabSideTexture);
            if(meta == 3) cir.setReturnValue(side <= 1 || isFullCube() ?
                    BlockParityHelper.cobblestoneTexture : BlockParityHelper.cobblestoneSlabSideTexture);
        }
    }
}
