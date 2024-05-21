package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.event.color.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;

public class TextureParityHelper {
    /**
     * stone, sandstone, planks, cobble
     */
    public static final int[] slabSideTextures = new int[4];
    /**
     * cross, powered cross
     */
    public static final int[] redstoneWireTextures = new int[2];

    @EventListener
    void registerTextures(TextureRegisterEvent event) {
        parity();
    }

    @EventListener
    void registerColorProvider(BlockColorsRegisterEvent event) {
    }

    @EventListener
    void registerColorProvider(ItemColorsRegisterEvent event) {
        event.itemColors.register(
                (item, tint) -> ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_13) >= 0 || !Multiproto.config.textureParity ? tint : -1,
                Block.LEAVES);
    }

    public static void parity() {
        if(!Multiproto.config.textureParity) return;
        ExpandableAtlas terrain = Atlases.getTerrain();
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_14) < 0) {
            Block.COBBLESTONE.textureId = terrain.addTexture(Multiproto.NAMESPACE.id("block/cobblestone")).index;
            Block.BRICKS.textureId = terrain.addTexture(Multiproto.NAMESPACE.id("block/bricks")).index;
            slabSideTextures[0] = terrain.addTexture(Multiproto.NAMESPACE.id("block/stone_slab_side")).index;
            slabSideTextures[1] = terrain.addTexture(Multiproto.NAMESPACE.id("block/sandstone_slab_side")).index;
            slabSideTextures[2] = terrain.addTexture(Multiproto.NAMESPACE.id("block/planks_slab_side")).index;
            slabSideTextures[3] = terrain.addTexture(Multiproto.NAMESPACE.id("block/cobblestone_slab_side")).index;
        } else {
            Block.COBBLESTONE.textureId = 16;
            Block.BRICKS.textureId = 7;
        }
        if(ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_9) < 0) {
            redstoneWireTextures[0] = terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_wire_cross")).index;
            terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_wire_line"));
            redstoneWireTextures[1] = terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_wire_cross_powered")).index;
            terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_wire_line_powered"));
        }
        Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (mc.worldRenderer != null) mc.worldRenderer.method_1537();
        Multiproto.LOGGER.info("Registered version parity textures");
    }
}
