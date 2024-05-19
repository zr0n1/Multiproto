package com.github.zr0n1.multiproto.mixin.parity.gui;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.Utils;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
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

    @Shadow private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "RETURN", shift = At.Shift.BY, by = -3))
    private void applyVersionNameParity(CallbackInfo ci) {
        String custom = Multiproto.config.customVersionName;
        if((!custom.isBlank() ||
                (Utils.getVersion().compareTo(ProtocolVersion.BETA_13) < 0 && Multiproto.config.showVersion)) &&
                !minecraft.options.debugHud) {
            GL11.glPushMatrix();
            minecraft.textRenderer.drawWithShadow("Minecraft " +
                    (custom.isBlank() ? Utils.getVersion().name(false) : custom), 2, 2, 16777215);
            GL11.glPopMatrix();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", remap = false),
    slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugHud:Z", ordinal = 0)))
    private void addDebugText(CallbackInfo ci) {
        ProtocolVersion v = Utils.getVersion();
        if(minecraft.isWorldRemote()) {
            minecraft.textRenderer.drawWithShadow("Protocol version: " + v.nameRange(true)
                    + " (" + v.version + ")", 2, (Utils.shouldApplyMojangFixStAPIDebugScreenIntegration() ? 116 : 100), 14737632);
        }
    }
}