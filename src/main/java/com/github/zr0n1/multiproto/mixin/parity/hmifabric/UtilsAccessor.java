package com.github.zr0n1.multiproto.mixin.parity.hmifabric;

import net.glasslauncher.hmifabric.Utils;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;

@Mixin(Utils.class)
public interface UtilsAccessor {

    @Accessor(remap = false)
    static ArrayList<ItemStack> getAllItems() {
        return null;
    }

    @Accessor(remap = false)
    static void setAllItems(ArrayList<ItemStack> allItems) {
    }
}
