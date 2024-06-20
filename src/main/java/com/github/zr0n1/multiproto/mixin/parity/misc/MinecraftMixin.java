package com.github.zr0n1.multiproto.mixin.parity.misc;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.VersionRegistry;
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

import static com.github.zr0n1.multiproto.protocol.ProtocolKt.*;

@Mixin(Minecraft.class)
@SuppressWarnings("deprecation")
public class MinecraftMixin {

    @Shadow
    private static Minecraft INSTANCE;

    @Inject(method = "method_2148", at = @At("HEAD"), cancellable = true)
    private static void multiproto_applyLightingParity(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((getCurrVer().isGE(BETA_9) || !Multiproto.config.lightingParity) &&
                INSTANCE != null && INSTANCE.options.ao);
    }

    @Inject(method = "method_2120", at = @At("HEAD"))
    private void multiproto_resetVersionOnSinglePlayer(CallbackInfo ci) {
        setCurrVer(VersionRegistry.INSTANCE.getVERSIONS$multiproto().last());
    }

    @Redirect(method = "run", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;fancyGraphics:Z"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;logGlError(Ljava/lang/String;)V")))
    private boolean multiproto_applyGrassSideParity(GameOptions options) {
        return getCurrVer().isGE(BETA_11) || (!Multiproto.config.textureParity && options.fancyGraphics);
    }
}
