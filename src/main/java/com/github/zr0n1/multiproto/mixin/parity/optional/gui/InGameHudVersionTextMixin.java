package com.github.zr0n1.multiproto.mixin.parity.optional.gui;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudVersionTextMixin extends DrawContext {

    @Shadow
    private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "RETURN", shift = At.Shift.BY, by = -3))
    private void applyVersionNameParity(CallbackInfo ci) {
        String custom = Multiproto.config.customVersionName;
        if ((!custom.isBlank() ||
                (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_13) && Multiproto.config.showVersion)) &&
                !minecraft.options.debugHud) {
            GL11.glPushMatrix();
            minecraft.textRenderer.drawWithShadow("Minecraft " +
                    (custom.isBlank() ? ProtocolVersionManager.getVersion().name(false) : custom), 2, 2, 16777215);
            GL11.glPopMatrix();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", remap = false),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugHud:Z", ordinal = 0)))
    private void addDebugText(CallbackInfo ci) {
        ProtocolVersion version = ProtocolVersionManager.getVersion();
        if (minecraft.isWorldRemote()) {
            minecraft.textRenderer.drawWithShadow("Protocol version: " + version.nameRange(true)
                    + " (" + version.version + ")",
                    2, (MultiprotoMixinPlugin.shouldApplyMojangFixStAPIDebugScreenIntegration() ? 116 : 100), 14737632);
        }
    }
}
