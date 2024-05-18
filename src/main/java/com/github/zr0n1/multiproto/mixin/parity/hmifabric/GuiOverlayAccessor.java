package com.github.zr0n1.multiproto.mixin.parity.hmifabric;

import net.glasslauncher.hmifabric.GuiOverlay;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;

@Mixin(GuiOverlay.class)
public interface GuiOverlayAccessor {
    @Accessor
    static void setCurrentItems(ArrayList<ItemStack> items) { }
    @Invoker
    static ArrayList<ItemStack> invokeGetCurrentList(ArrayList<ItemStack> listToSearch) { return null; }
}
