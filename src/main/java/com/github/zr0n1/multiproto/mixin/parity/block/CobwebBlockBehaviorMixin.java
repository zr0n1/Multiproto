package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.protocol.Protocol;
import com.github.zr0n1.multiproto.protocol.Version;
import net.minecraft.block.CobwebBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CobwebBlock.class)
public abstract class CobwebBlockBehaviorMixin {

    @Inject(method = "isFullCube", at = @At("HEAD"), cancellable = true)
    private void multiproto_applyIsFullCubeParity(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Protocol.getVer() == Version.B1_6_6);
    }
}
