package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin;
import com.github.zr0n1.multiproto.mixin.parity.item.ToolItemAccessor;
import com.github.zr0n1.multiproto.parity.hmifabric.HMIFabricIntegrationHelper;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

public class ItemParityHelper {

    public static void applyParity() {
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
