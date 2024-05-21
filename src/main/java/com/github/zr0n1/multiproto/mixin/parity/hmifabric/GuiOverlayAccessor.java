package com.github.zr0n1.multiproto.mixin.parity.hmifabric;

import net.glasslauncher.hmifabric.GuiOverlay;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;

@Mixin(GuiOverlay.class)
public interface GuiOverlayAccessor {

    @Accessor(remap = false)
    static void setCurrentItems(ArrayList<ItemStack> items) {
    }

    @Invoker(remap = false)
    static ArrayList<ItemStack> invokeGetCurrentList(ArrayList<ItemStack> listToSearch) {
        return null;
    }
}
