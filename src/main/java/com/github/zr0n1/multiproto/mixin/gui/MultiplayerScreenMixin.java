package com.github.zr0n1.multiproto.mixin.gui;

import com.github.zr0n1.multiproto.gui.VersionScreen;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton(CallbackInfo ci) {
        ButtonWidget b = (ButtonWidget) buttons.get(0);
        buttons.add(new ButtonWidget(100, b.x, b.y - 24,
                "Protocol version: " + VersionManager.getLastVersion().nameRange(true)));
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    private void onCustomButtonClicked(ButtonWidget b, CallbackInfo ci) {
        if (b.id == 100) {
            minecraft.setScreen(new VersionScreen(this));
            ci.cancel();
        }
    }

    @Inject(method = "buttonClicked", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;save()V")))
    private void setVersionOnConnect(ButtonWidget button, CallbackInfo ci) {
        VersionManager.setVersion(VersionManager.getLastVersion());
    }
}
