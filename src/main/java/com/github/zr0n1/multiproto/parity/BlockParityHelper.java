package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin;
import com.github.zr0n1.multiproto.mixin.parity.block.BlockAccessor;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class BlockParityHelper {

    public static void applyParity() {
        // reset cobweb fields
        ((BlockAccessor) Block.COBWEB).setMaterial(Material.COBWEB);
        Block.COBWEB.setHardness(4F);
        Block.COBWEB.setOpacity(1);
        Block.BLOCKS_OPAQUE[Block.COBWEB.id] = Block.COBWEB.isOpaque();
        // reset glowstone fields
        ((BlockAccessor) Block.GLOWSTONE).setMaterial(Material.STONE);
        Block.GLOWSTONE.setHardness(0.3F);
        ProtocolVersion version = ProtocolVersionManager.getVersion();
        // < b1.7
        if (version.compareTo(ProtocolVersion.BETA_14) < 0) {
            ((BlockAccessor) Block.COBWEB).setMaterial(Material.WOOL);
            Block.COBWEB.setHardness(0F);
            Block.COBWEB.setOpacity(0);
            Block.BLOCKS_OPAQUE[Block.COBWEB.id] = false;
        }
        // < b1.6
        if (version.compareTo(ProtocolVersion.BETA_13) < 0) {
            ((BlockAccessor) Block.GLOWSTONE).setMaterial(Material.GLASS);
            // No Idea Why Vanilla Hardness Doesnt Work Since It Didnt Change But Okay
            Block.GLOWSTONE.setHardness(0.1F);
        }
        Multiproto.LOGGER.info("Applied version block parity");
    }
}
