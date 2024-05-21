package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.MultiplayerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.MultiplayerServerListWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(MultiplayerServerListWidget.class)
public abstract class MultiplayerServerListWidgetMixin {

    @Shadow(remap = false)
    private @Final MultiplayerScreen parent;

    @Inject(method = "renderEntry", at = @At("TAIL"))
    private void addVersionText(int i, int x, int y, int l, Tessellator arg, CallbackInfo ci, @Local ServerData server) {
        this.parent.drawTextWithShadow(this.parent.getFontRenderer(),
                ((MultiprotoServerData) server).multiproto_getVersion().nameRange(true), x + 2, y + 23, 8421504);
    }
}
