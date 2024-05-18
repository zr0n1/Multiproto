package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.parity.block.BlockAccessor;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class BlockParityHelper {

    public static int cobblestoneTexture;
    public static int bricksTexture;
    public static int sandstoneSlabSideTexture;
    public static int stoneSlabSideTexture;
    public static int planksSlabSideTexture;
    public static int cobblestoneSlabSideTexture;

    public static ArrayList<Item> removed = new ArrayList<>();

    public static void parity() {
        removed.clear();
        visualParity();
        // reset cobweb fields
        ((BlockAccessor)Block.COBWEB).setMaterial(Material.COBWEB);
        Block.COBWEB.setOpacity(1);
        // reset glowstone fields
        ((BlockAccessor)Block.GLOWSTONE).setMaterial(Material.STONE);
        Block.BLOCKS_OPAQUE[Block.GLOWSTONE.id] = Block.GLOWSTONE.isOpaque();
        ProtocolVersion version = Multiproto.getVersion();
        // < b1.7
        if(version.compareTo(ProtocolVersion.BETA_14) < 0) {
            ((BlockAccessor)Block.COBWEB).setMaterial(Material.WOOL);
            Block.COBWEB.setHardness(0.0F);
            Block.COBWEB.setOpacity(0);
        }
        // < b1.6
        if(version.compareTo(ProtocolVersion.BETA_13) < 0) {
            ((BlockAccessor) Block.GLOWSTONE).setMaterial(Material.GLASS);
        }
        Multiproto.LOGGER.info("Applied version block parity");
        // hmifabric integration
        if(Multiproto.shouldApplyHMIFabricIntegration()) {
            // < b1.7
            removeBefore(Block.PISTON, ProtocolVersion.BETA_14);
            removeBefore(Block.STICKY_PISTON, ProtocolVersion.BETA_14);
            removeBefore(Block.PISTON_HEAD, ProtocolVersion.BETA_14);
            removeBefore(Block.MOVING_PISTON, ProtocolVersion.BETA_14);
            // < b1.6
            removeBefore(Block.DEAD_BUSH, ProtocolVersion.BETA_13);
            removeBefore(Block.GRASS, ProtocolVersion.BETA_13);
            removeBefore(Block.TRAPDOOR, ProtocolVersion.BETA_13);
            // < b1.5
            removeBefore(Block.COBWEB, ProtocolVersion.BETA_11);
            removeBefore(Block.DETECTOR_RAIL, ProtocolVersion.BETA_11);
            removeBefore(Block.POWERED_RAIL, ProtocolVersion.BETA_11);
            // < b1.3
            removeBefore(Block.BED, ProtocolVersion.BETA_9);
            removeBefore(Block.REPEATER, ProtocolVersion.BETA_9);
            removeBefore(Block.POWERED_REPEATER, ProtocolVersion.BETA_9);
            // < b1.2
            removeBefore(Block.CAKE, ProtocolVersion.BETA_8);
            removeBefore(Block.DISPENSER, ProtocolVersion.BETA_8);
            removeBefore(Block.LAPIS_BLOCK, ProtocolVersion.BETA_8);
            removeBefore(Block.LAPIS_ORE, ProtocolVersion.BETA_8);
            removeBefore(Block.NOTE_BLOCK, ProtocolVersion.BETA_8);
            removeBefore(Block.SANDSTONE, ProtocolVersion.BETA_8);
        }
    }

    public static void visualParity() {
        Block.COBBLESTONE.textureId = 16;
        Block.BRICKS.textureId = 7;
        // < b1.7
        if(!Multiproto.config.visualParity) return;
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_14) < 0) {
            Block.COBBLESTONE.textureId = BlockParityHelper.cobblestoneTexture;
            Block.BRICKS.textureId = BlockParityHelper.bricksTexture;
        }
    }

    public static void removeBefore(Block block, ProtocolVersion version) {
        if(Multiproto.getVersion().compareTo(version) < 0) removed.add(Item.ITEMS[block.id]);
    }
}