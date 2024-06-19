package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.gui.VersionScreen;
import com.github.zr0n1.multiproto.protocol.ProtocolKt;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.DirectConnectScreen;
import static com.github.zr0n1.multiproto.protocol.ProtocolKt.*;

@Mixin(DirectConnectScreen.class)
public abstract class DirectConnectScreenMixin extends Screen {

    @Inject(method = "lambda$init$0", at = @At(value = "INVOKE",
            target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/DirectConnectScreen;" +
                    "connect(Lnet/minecraft/client/Minecraft;Ljava/lang/String;)V"), remap = false)
    private void multiproto_setVersionOnConnect(CallbackButtonWidget button, CallbackInfo ci) {
        setCurrVer(ProtocolKt.getLastVer());
    }

    @Inject(method = "init", at = @At("TAIL"))
    @SuppressWarnings("unchecked")
    private void multiproto_addVersionButton(CallbackInfo ci) {
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
                "Protocol version: " + getLastVer().nameRange(true),
                button -> minecraft.setScreen(new VersionScreen(this))));
    }

}
