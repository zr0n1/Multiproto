package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.parity.item.ToolItemAccessor;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.item.*;

public class ItemParityHelper {

    public static void applyParity() {
        // set tool durability and speed lower < b1.2
        for (Item item : Item.ITEMS) {
            if (item instanceof ToolItem tool) {
                ToolMaterial material = tool.getMaterial(new ItemStack(item));
                tool.setMaxDamage((ProtocolVersionManager.isBefore(ProtocolVersion.BETA_8) ?
                        (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1) : material.getDurability()));
                ((ToolItemAccessor) tool).setMiningSpeed(ProtocolVersionManager.isBefore(ProtocolVersion.BETA_8) ?
                        (material.getMiningLevel() + 1) * 2 : material.getMiningSpeedMultiplier());
            } else if (item instanceof SwordItem sword) {
                ToolMaterial material = sword.getMaterial(new ItemStack(item));
                sword.setMaxDamage((ProtocolVersionManager.isBefore(ProtocolVersion.BETA_8) ?
                        (32 << material.getMiningLevel()) * (material.getMiningLevel() == 3 ? 4 : 1) : material.getDurability()));
            }
        }
        Multiproto.LOGGER.info("Applied version item parity");
    }
}
