package com.github.zr0n1.multiproto.mixin.gui;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntryListWidget.class)
public interface EntryListWidgetAccessor {
    @Accessor
    @SuppressWarnings("unused")
    void setScrollAmount(float f);
}
