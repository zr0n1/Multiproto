package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.gui.ChangeVersionScreen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.DirectConnectScreen;

@Mixin(DirectConnectScreen.class)
public abstract class DirectConnectScreenMixin extends Screen {

    @Inject(method = "init", at = @At("RETURN"))
    private void addButton(CallbackInfo ci) {
        Multiproto.loadLastVersion();
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
            I18n.getTranslation("multiproto.gui.changeVersion") + ": " +
                    Multiproto.getVersion().nameRange(true),
                (button) -> {
                minecraft.setScreen(new ChangeVersionScreen(this));
        }));
    }

}
