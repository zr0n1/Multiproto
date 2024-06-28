package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.gui.VersionScreen;
import com.github.zr0n1.multiproto.mixinterface.MultiprotoEditServerScreen;
import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import com.github.zr0n1.multiproto.protocol.Version;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.EditServerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(EditServerScreen.class)
public abstract class EditServerScreenMixin extends Screen implements MultiprotoEditServerScreen {
    @Shadow(remap = false)
    private @Final ServerData server;

    @Unique
    private Version multiproto$version = Version.B1_7_3;

    @Override
    @Unique
    public Version multiproto_getVersion() {
        return server != null ? ((MultiprotoServerData)server).multiproto_getVersion() : multiproto$version;
    }

    @Override
    @Unique
    public void multiproto_setVersion(Version ver) {
        if(server != null) ((MultiprotoServerData)server).multiproto_setVersion(ver); else multiproto$version = ver;
    }

    @Inject(method = "init", at = @At("TAIL"))
    @SuppressWarnings("unchecked")
    private void multiproto_button(CallbackInfo ci) {
        buttons.add(new CallbackButtonWidget(width / 2 - 100, height / 4 + 72 + 12,
                "Protocol version: " + multiproto_getVersion().name(true),
                button -> minecraft.setScreen(new VersionScreen(this))));
    }

    @Redirect(method = "lambda$init$0", at = @At(value = "NEW",
            target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/ServerData;", remap = false), remap = false)
    private ServerData multiproto_serverData(String name, String ip) {
        return MultiprotoServerData.create(name, ip, multiproto$version);
    }
}
