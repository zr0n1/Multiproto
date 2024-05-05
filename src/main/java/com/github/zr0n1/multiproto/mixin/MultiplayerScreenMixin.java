package com.github.zr0n1.multiproto.mixin;

import com.github.zr0n1.multiproto.gui.ChangeVersionScreen;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        ButtonWidget b = (ButtonWidget)buttons.get(0);
        buttons.add(new ButtonWidget(100, b.x, b.y - 24,
                I18n.getTranslation("multiproto.button.changeVersion") + ": " +
                        ProtocolVersion.getCurrentVersion().nameRange(true)));
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"), cancellable = true)
    private void buttonClicked(ButtonWidget b, CallbackInfo ci) {
        if(b.id == 100) {
            minecraft.setScreen(new ChangeVersionScreen(this));
            ci.cancel();
        }
    }
}
