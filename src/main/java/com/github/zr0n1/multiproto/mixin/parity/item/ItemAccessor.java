package com.github.zr0n1.multiproto.mixin.parity.item;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemAccessor {
    @Accessor
    int getTextureId();

    @Accessor("translationKey")
    void setRawTranslationKey(String key);

    @Accessor
    void setHandheld(boolean isHandheld);
}
