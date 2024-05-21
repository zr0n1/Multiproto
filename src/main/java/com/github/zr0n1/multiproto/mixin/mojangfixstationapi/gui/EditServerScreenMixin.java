package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.gui.ProtocolVersionScreen;
import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.EditServerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.MultiplayerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(EditServerScreen.class)
public abstract class EditServerScreenMixin extends Screen {

    @Shadow(remap = false)
    private @Final ServerData server;

    @Shadow(remap = false)
    private @Final MultiplayerScreen parent;

    @Redirect(method = "lambda$init$0", at = @At(value = "NEW", target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/ServerData;"))
    private ServerData redirectMultiprotoServerData(String name, String ip) {
        return MultiprotoServerData.create(name, ip, ProtocolVersionScreen.getLastServerVersion());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton(CallbackInfo ci) {
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
                "Protocol version: " + (server != null ?
                        ((MultiprotoServerData) server).multiproto_getVersion().nameRange(true) :
                        ProtocolVersionScreen.getLastServerVersion().nameRange(true)), (button) -> {
            minecraft.setScreen(new ProtocolVersionScreen(this, server));
        }));
    }
}
