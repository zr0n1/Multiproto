package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.github.zr0n1.multiproto.gui.ProtocolVersionScreen;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.DirectConnectScreen;

@Mixin(DirectConnectScreen.class)
public abstract class DirectConnectScreenMixin extends Screen {

    @Inject(method = "init", at = @At("TAIL"))
    private void addButton(CallbackInfo ci) {
        ProtocolVersionManager.loadLastVersion();
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
            "Protocol version: " +
                    ProtocolVersionManager.getVersion().nameRange(true),
                (button) -> {
                minecraft.setScreen(new ProtocolVersionScreen(this));
        }));
    }

}
