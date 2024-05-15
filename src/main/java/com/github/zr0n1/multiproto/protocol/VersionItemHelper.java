package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.mixin.item.ToolItemAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

public class VersionItemHelper {

    public static void applyChanges() {
        ProtocolVersion v = ProtocolVersionManager.getCurrentVersion();
        for(Item item : Item.ITEMS) {
            if(item instanceof ToolItem) {
                ToolMaterial material = getToolMaterial(item);
                item.setMaxDamage((v.compareTo(ProtocolVersion.BETA_8) >= 0 ?
                        material.getDurability() : (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1)));
                ((ToolItemAccessor)item).setMiningSpeed(v.compareTo(ProtocolVersion.BETA_8) >= 0 ?
                        material.getMiningSpeedMultiplier() : (material.getMiningLevel() + 1) * 2);
            }
            if(item instanceof SwordItem) {
                ToolMaterial material = getToolMaterial(item);
                item.setMaxDamage((v.compareTo(ProtocolVersion.BETA_8) >= 0 ?
                        material.getDurability() : (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1)));
            }
        }
    }

    private static ToolMaterial getToolMaterial(Item item) {
        if(item instanceof ToolItem) return ((ToolItemAccessor)item).getToolMaterial();
        ToolMaterial material = ToolMaterial.IRON;
        if(item == Item.WOODEN_SWORD) {
            material = ToolMaterial.WOOD;
        } else if(item == Item.STONE_SWORD) {
            material = ToolMaterial.STONE;
        } else if(item == Item.DIAMOND_SWORD) {
            material = ToolMaterial.DIAMOND;
        } else if(item == Item.GOLDEN_SWORD) {
            material = ToolMaterial.GOLD;
        }
        return material;
    }
}
