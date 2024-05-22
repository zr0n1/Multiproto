package com.github.zr0n1.multiproto.parity.optional;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class TranslationParityHelper {

    public static void applyParity() {
        replaceBefore(ProtocolVersion.BETA_9, Item.GUNPOWDER, "sulphur");
        replaceBefore(ProtocolVersion.BETA_8, Block.CRAFTING_TABLE, "workbench");
        replaceBefore(ProtocolVersion.BETA_8, Block.SUGAR_CANE, "reeds");
        replaceBefore(ProtocolVersion.BETA_8, Item.SUGAR_CANE, "reeds");
    }

    public static void replaceBefore(ProtocolVersion target, Block block, String key) {
        block.setTranslationKey((ProtocolVersionManager.isBefore(target) && Multiproto.config.translationParity ? "multiproto." : "") + key);
    }

    public static void replaceBefore(ProtocolVersion target, Item item, String key) {
        item.setTranslationKey((ProtocolVersionManager.isBefore(target) && Multiproto.config.translationParity ? "multiproto." : "") + key);
    }
}
