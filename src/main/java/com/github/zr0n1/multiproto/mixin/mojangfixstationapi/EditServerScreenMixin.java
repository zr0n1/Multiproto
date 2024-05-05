package com.github.zr0n1.multiproto.mixin.mojangfixstationapi;

import com.github.zr0n1.multiproto.gui.ChangeVersionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.EditServerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(EditServerScreen.class)
public class EditServerScreenMixin extends Screen {

    @Shadow
    private @Final ServerData server;

    @Inject(method = "init", at = @At("RETURN"))
    private void addButton(CallbackInfo ci) {
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
            I18n.translate("multiplayer.multiproto:changeVersion"), (button) -> {
                minecraft.openScreen(new ChangeVersionScreen(this));
        }));
    }
}
