package com.github.zr0n1.multiproto.mixin.gui;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiplayerScreen.class)
public interface MultiplayerScreenAccessor {
    @Accessor("field_1658")
    TextFieldWidget getAddressField();
}
