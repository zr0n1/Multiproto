package com.github.zr0n1.multiproto.mixin.misc;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.fabricmc.loader.api.FabricLoader;
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

    @Shadow private static Minecraft INSTANCE;

    @Inject(method = "method_2120", at = @At("HEAD"))
    private void joinSinglePlayerWorld(CallbackInfo ci) {
        ProtocolVersionManager.setCurrentVersion(ProtocolVersion.BETA_14);
    }

    @Inject(method = "method_2148", at = @At("HEAD"), cancellable = true)
    private static void versionGraphicsAo(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!(Multiproto.config.versionGraphics &&
                ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_9) < 0) &&
                INSTANCE != null && INSTANCE.options.ao);
    }

    @Redirect(method = "run", at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;fancyGraphics:Z"),
    slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;logGlError(Ljava/lang/String;)V")))
    private boolean redirectBlockRendererFancyGraphics(GameOptions options) {
        return !(Multiproto.config.versionGraphics && ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_11) < 0) &&
                options.fancyGraphics;
    }
}
