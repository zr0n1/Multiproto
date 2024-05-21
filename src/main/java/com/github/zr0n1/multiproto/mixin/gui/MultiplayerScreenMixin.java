package com.github.zr0n1.multiproto.mixin.gui;

import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.github.zr0n1.multiproto.gui.ProtocolVersionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        ProtocolVersionManager.loadLastVersion();
        ButtonWidget b = (ButtonWidget)buttons.get(0);
        buttons.add(new ButtonWidget(100, b.x, b.y - 24,
                "Protocol version: " + ProtocolVersionManager.getVersion().nameRange(true)));
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    private void buttonClicked(ButtonWidget b, CallbackInfo ci) {
        if(b.id == 100) {
            minecraft.setScreen(new ProtocolVersionScreen(this));
            ci.cancel();
        }
    }
}
