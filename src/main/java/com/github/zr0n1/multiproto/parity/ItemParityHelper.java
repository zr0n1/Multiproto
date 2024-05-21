package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin;
import com.github.zr0n1.multiproto.mixin.parity.hmifabric.GuiOverlayAccessor;
import com.github.zr0n1.multiproto.mixin.parity.hmifabric.UtilsAccessor;
import com.github.zr0n1.multiproto.mixin.parity.item.ToolItemAccessor;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

import java.util.ArrayList;

public class ItemParityHelper {

    public static ArrayList<Item> removed = new ArrayList<>();

    public static void parity() {
        removed.clear();
        removed.addAll(BlockParityHelper.removed);
        ProtocolVersion version = ProtocolVersionManager.getVersion();
        // tools and swords
        for (Item item : Item.ITEMS) {
            if (item instanceof ToolItem) {
                ToolMaterial material = getToolMaterial(item);
                item.setMaxDamage((version.compareTo(ProtocolVersion.BETA_8) >= 0 ?
                        material.getDurability() : (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1)));
                ((ToolItemAccessor) item).setMiningSpeed(version.compareTo(ProtocolVersion.BETA_8) >= 0 ?
                        material.getMiningSpeedMultiplier() : (material.getMiningLevel() + 1) * 2);
            }

            if (item instanceof SwordItem) {
                ToolMaterial material = getToolMaterial(item);
                item.setMaxDamage((version.compareTo(ProtocolVersion.BETA_8) >= 0 ?
                        material.getDurability() : (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1)));
            }
        }
        Multiproto.LOGGER.info("Applied version item parity");
        if (MultiprotoMixinPlugin.shouldApplyHMIFabricIntegration()) applyHMIFabricIntegration();
    }

    public static void applyHMIFabricIntegration() {
        // < b1.7
        removeBefore(Item.SHEARS, ProtocolVersion.BETA_14);
        // < b1.6
        removeBefore(Item.MAP, ProtocolVersion.BETA_13);
        // < b1.4
        removeBefore(Item.COOKIE, ProtocolVersion.BETA_10);
        // < b1.3
        removeBefore(Item.REPEATER, ProtocolVersion.BETA_9);
        removeBefore(Item.BED, ProtocolVersion.BETA_9);
        // < b1.2
        removeBefore(Item.CAKE, ProtocolVersion.BETA_8);
        removeBefore(Item.BONE, ProtocolVersion.BETA_8);
        removeBefore(Item.SUGAR, ProtocolVersion.BETA_8);
        removeBefore(Item.DYE, ProtocolVersion.BETA_8);
        if (UtilsAccessor.getAllItems() != null) {
            UtilsAccessor.setAllItems(null);
            GuiOverlayAccessor.setCurrentItems(GuiOverlayAccessor.invokeGetCurrentList(net.glasslauncher.hmifabric.Utils.itemList()));
        }
        Multiproto.LOGGER.info("Removed {} blocks, {} items from HMI-Fabric", BlockParityHelper.removed.size(),
                ItemParityHelper.removed.size() - BlockParityHelper.removed.size());
    }

    public static void removeBefore(Item item, ProtocolVersion version) {
        if (ProtocolVersionManager.getVersion().compareTo(version) < 0) removed.add(item);
    }

    public static ToolMaterial getToolMaterial(Item item) {
        if (item instanceof ToolItem) return ((ToolItemAccessor) item).getToolMaterial();
        ToolMaterial material = ToolMaterial.IRON;
        if (item == Item.WOODEN_SWORD) {
            material = ToolMaterial.WOOD;
        } else if (item == Item.STONE_SWORD) {
            material = ToolMaterial.STONE;
        } else if (item == Item.DIAMOND_SWORD) {
            material = ToolMaterial.DIAMOND;
        } else if (item == Item.GOLDEN_SWORD) {
            material = ToolMaterial.GOLD;
        }
        return material;
    }
}
