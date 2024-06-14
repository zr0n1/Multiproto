package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.parity.item.ToolItemAccessor;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.item.*;

public class ItemHelper {

    public static void applyChanges() {
        // set tool durability and speed lower < b1.2
        for (Item item : Item.ITEMS) {
            if (item instanceof ToolItem tool) {
                ToolMaterial material = tool.getMaterial(new ItemStack(item));
                tool.setMaxDamage((VersionManager.isLT(Version.BETA_8) ?
                        (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1) : material.getDurability()));
                ((ToolItemAccessor) tool).setMiningSpeed(VersionManager.isLT(Version.BETA_8) ?
                        (material.getMiningLevel() + 1) * 2 : material.getMiningSpeedMultiplier());
            } else if (item instanceof SwordItem sword) {
                ToolMaterial material = sword.getMaterial(new ItemStack(item));
                sword.setMaxDamage((VersionManager.isLT(Version.BETA_8) ?
                        (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1) : material.getDurability()));
            }
        }
        Multiproto.LOGGER.info("Applied version item parity");
    }
}
