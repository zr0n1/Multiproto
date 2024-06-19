package com.github.zr0n1.multiproto.parity.optional;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class TranslationHelper {

    public static void applyChanges() {
        replaceBefore(Version.BETA_9, Item.GUNPOWDER, "sulphur");
        replaceBefore(Version.BETA_8, Block.CRAFTING_TABLE, "workbench");
        replaceBefore(Version.BETA_8, Block.SUGAR_CANE, "reeds");
        replaceBefore(Version.BETA_8, Item.SUGAR_CANE, "reeds");
    }

    public static void replaceBefore(Version target, Block block, String key) {
        block.setTranslationKey((VersionManager.isLT(target) && Multiproto.config.translationParity ? "multiproto." : "") + key);
    }

    public static void replaceBefore(Version target, Item item, String key) {
        item.setTranslationKey((VersionManager.isLT(target) && Multiproto.config.translationParity ? "multiproto." : "") + key);
    }
}
