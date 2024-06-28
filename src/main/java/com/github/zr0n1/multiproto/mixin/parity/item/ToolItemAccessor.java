package com.github.zr0n1.multiproto.mixin.parity.item;

import net.minecraft.block.Block;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToolItem.class)
public interface ToolItemAccessor {
    @Accessor
    Block[] getEffectiveOnBlocks();
    @Accessor
    void setEffectiveOnBlocks(Block[] blocks);

    @Accessor
    float getMiningSpeed();
    @Accessor
    void setMiningSpeed(float f);

    @Accessor
    int getDamage();
    @Accessor
    void setDamage(int i);
}
