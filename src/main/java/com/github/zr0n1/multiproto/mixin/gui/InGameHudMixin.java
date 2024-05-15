package com.github.zr0n1.multiproto.mixin.gui;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.fabricmc.loader.api.FabricLoader;
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
public abstract class InGameHudMixin extends DrawContext {
    @Shadow private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "RETURN", shift = At.Shift.BY, by = -3))
    private void addVersionText(CallbackInfo ci) {
        if(ProtocolVersionManager.getCurrentVersion().compareTo(ProtocolVersion.BETA_13) < 0 &&
                Multiproto.config.showVersion && !minecraft.options.debugHud) {
            GL11.glPushMatrix();
            minecraft.textRenderer.drawWithShadow("Minecraft " + ProtocolVersionManager.getCurrentVersion().name(),
                    2, 2, 16777215);
            GL11.glPopMatrix();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glPopMatrix()V", remap = false),
    slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;debugHud:Z", ordinal = 0)))
    private void addDebugText(CallbackInfo ci) {
        ProtocolVersion v = ProtocolVersionManager.getCurrentVersion();
        if(minecraft.isWorldRemote()) {
            minecraft.textRenderer.drawWithShadow("Protocol version: " + v.nameRange(true)
                    + " (" + v.version + ")", 2, (FabricLoader.getInstance().isModLoaded("mojangfixstationapi") ? 116 : 100), 14737632);
        }
    }
}
