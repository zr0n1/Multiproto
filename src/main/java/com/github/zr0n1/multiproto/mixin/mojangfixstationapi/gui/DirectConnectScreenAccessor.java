package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import pl.telvarost.mojangfixstationapi.client.gui.CallbackButtonWidget;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.DirectConnectScreen;

@Mixin(DirectConnectScreen.class)
public interface DirectConnectScreenAccessor {
    @Accessor(remap = false)
    TextFieldWidget getAddressField();

    @Accessor(remap = false)
    CallbackButtonWidget getConnectButton();
}
