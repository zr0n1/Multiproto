package com.github.zr0n1.multiproto.parity.hmifabric;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.parity.hmifabric.UtilsAccessor;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.glasslauncher.hmifabric.GuiOverlay;
import net.glasslauncher.hmifabric.event.HMIItemListRefreshEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.item.ItemConvertible;

import java.util.ArrayList;

public class HMIFabricIntegrationHelper implements HMIItemListRefreshEvent {

    public static ArrayList<Item> removed = new ArrayList<>();

    @Override
    public void refreshItemList(ArrayList<ItemStack> stacks) {
        stacks.removeIf(stack -> removed.contains(stack.getItem()) ||
                (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) && stack.getDamage() > 0);
    }

    public static void applyParity() {
        removed.clear();
        // < b1.7
        removeBefore(ProtocolVersion.BETA_14,
                Block.PISTON, Block.STICKY_PISTON,
                Item.SHEARS);
        // < b1.6
        removeBefore(ProtocolVersion.BETA_13,
                Block.DEAD_BUSH,
                Block.GRASS,
                Block.TRAPDOOR,
                Item.MAP);
        // < b1.5
        removeBefore(ProtocolVersion.BETA_11,
                Block.COBWEB,
                Block.DETECTOR_RAIL, Block.POWERED_RAIL);
        // < b1.4
        removeBefore(ProtocolVersion.BETA_10, Item.COOKIE);
        // < b1.3
        removeBefore(ProtocolVersion.BETA_9,
                Block.BED, Item.BED,
                Block.REPEATER, Item.REPEATER);
        // < b1.2
        removeBefore(ProtocolVersion.BETA_8,
                Block.CAKE, Item.CAKE,
                Block.DISPENSER,
                Block.LAPIS_BLOCK, Block.LAPIS_ORE,
                Block.NOTE_BLOCK,
                Block.SANDSTONE,
                Item.BONE,
                Item.DYE,
                Item.SUGAR);
        if (UtilsAccessor.getAllItems() != null) {
            UtilsAccessor.setAllItems(null);
            GuiOverlay.resetItems();
        }
        Multiproto.LOGGER.info("Removed {} entries from HowManyItems-Fabric", removed.size());
    }

    public static void removeBefore(ProtocolVersion version, ItemConvertible... items) {
        for (ItemConvertible item : items) {
            if (ProtocolVersionManager.getVersion().compareTo(version) < 0) removed.add(item.asItem());
        }
    }

    public static void removeBefore(ItemConvertible item, ProtocolVersion version) {
        if (ProtocolVersionManager.getVersion().compareTo(version) < 0) removed.add(item.asItem());
    }
}
