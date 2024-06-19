package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.EditServerScreen;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(EditServerScreen.class)
public interface EditServerScreenAccessor {
    @Accessor
    ServerData getServer();

    @Accessor
    ButtonWidget getButton();

    @Accessor
    TextFieldWidget getNameTextField();

    @Accessor
    TextFieldWidget getIpTextField();
}
