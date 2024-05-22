package com.github.zr0n1.multiproto.mixin.parity.misc;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    private static Minecraft INSTANCE;

    @Inject(method = "method_2148", at = @At("HEAD"), cancellable = true)
    private static void applyLightingParity(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!(Multiproto.config.lightingParity &&
                ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9)) &&
                INSTANCE != null && INSTANCE.options.ao);
    }

    @Inject(method = "method_2120", at = @At("HEAD"))
    private void joinSinglePlayerWorld(CallbackInfo ci) {
        ProtocolVersionManager.setVersion(ProtocolVersion.BETA_14);
    }

    @Redirect(method = "run", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;fancyGraphics:Z"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;logGlError(Ljava/lang/String;)V")))
    private boolean applyFancyGrassParity(GameOptions options) {
        return !(Multiproto.config.textureParity && ProtocolVersionManager.isBefore(ProtocolVersion.BETA_11)) &&
                options.fancyGraphics;
    }
}
