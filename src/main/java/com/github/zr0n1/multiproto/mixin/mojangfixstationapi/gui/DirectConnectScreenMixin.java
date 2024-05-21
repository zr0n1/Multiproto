package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.gui.ProtocolVersionScreen;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.DirectConnectScreen;

@Mixin(DirectConnectScreen.class)
public abstract class DirectConnectScreenMixin extends Screen {

    @Inject(method = "lambda$init$0", at = @At(value = "INVOKE",
            target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/DirectConnectScreen;" +
                    "connect(Lnet/minecraft/client/Minecraft;Ljava/lang/String;)V"), remap = false)
    private void setVersionOnConnect(CallbackButtonWidget button, CallbackInfo ci) {
        ProtocolVersionManager.setVersion(ProtocolVersionManager.getLastVersion());
    }

    @Inject(method = "init", at = @At("TAIL"), remap = false)
    private void addCustomButton(CallbackInfo ci) {
        ProtocolVersionManager.getLastVersion();
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
                "Protocol version: " +
                        ProtocolVersionManager.getLastVersion().nameRange(true),
                button -> minecraft.setScreen(new ProtocolVersionScreen(this))));
    }

}
