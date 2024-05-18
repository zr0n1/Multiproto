package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.block.CobwebBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CobwebBlock.class)
public class CobwebBlockMixin {

    @Inject(method = "isFullCube", at = @At("HEAD"), cancellable = true)
    private void isFullCube(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(Multiproto.getVersion() == ProtocolVersion.BETA_13);
    }
}
