package com.github.zr0n1.multiproto.mixin.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.VersionGraphicsHelper;
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
    private void getTexture(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_14) < 0 &&
                Multiproto.config.versionGraphics) {
            if(!isFullCube() && meta == 0 && side > 1) cir.setReturnValue(VersionGraphicsHelper.stoneSlabSideTexture);
            if(!isFullCube() && meta == 1 && side > 1) cir.setReturnValue(VersionGraphicsHelper.sandstoneSlabSideTexture);
            if(!isFullCube() && meta == 2 && side > 1) cir.setReturnValue(VersionGraphicsHelper.planksSlabSideTexture);
            if(meta == 3) cir.setReturnValue(side <= 1 || isFullCube() ?
                    VersionGraphicsHelper.cobblestoneTexture : VersionGraphicsHelper.cobblestoneSlabSideTexture);
        }
    }
}
