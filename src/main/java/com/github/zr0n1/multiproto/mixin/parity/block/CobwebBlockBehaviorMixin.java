package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.block.CobwebBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CobwebBlock.class)
public abstract class CobwebBlockBehaviorMixin {

    @Inject(method = "isFullCube", at = @At("HEAD"), cancellable = true)
    private void applyCobwebFullCubeParity(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(VersionManager.getVersion() == Version.BETA_13);
    }
}
