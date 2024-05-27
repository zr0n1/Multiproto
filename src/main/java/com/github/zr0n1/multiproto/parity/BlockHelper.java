package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.parity.block.BlockAccessor;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockHelper {

    public static void applyChanges() {
        // set cobweb fields
        ((BlockAccessor) Block.COBWEB).setMaterial(VersionManager.isBefore(Version.BETA_14) ? Material.WOOL : Material.COBWEB);
        Block.COBWEB.setHardness(VersionManager.isBefore(Version.BETA_14) ? 0F : 4F);
        Block.COBWEB.setOpacity(VersionManager.isBefore(Version.BETA_14) ? 0 : 1);
        Block.BLOCKS_OPAQUE[Block.COBWEB.id] = VersionManager.isBefore(Version.BETA_14);
        // set glowstone fields
        ((BlockAccessor) Block.GLOWSTONE).setMaterial(VersionManager.isBefore(Version.BETA_13) ? Material.GLASS : Material.STONE);
        Block.GLOWSTONE.setHardness(VersionManager.isBefore(Version.BETA_14) ? 0.1F : 0.3F);
        Multiproto.LOGGER.info("Applied version block parity");
    }
}
