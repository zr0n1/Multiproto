package com.github.zr0n1.multiproto.parity.optional;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;

public class TextureParityHelper {

    /**
     * smooth stone, sandstone, planks, cobblestone
     */
    public static final int[] slabSideTextures = new int[4];
    /**
     * cross off, cross on
     */
    public static final int[] redstoneWireTextures = new int[2];

    @EventListener
    void registerTextures(TextureRegisterEvent event) {
        applyParity();
    }

    public static void applyParity() {
        ExpandableAtlas terrain = Atlases.getTerrain();
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_14) && Multiproto.config.textureParity) {
            Block.BRICKS.textureId = terrain.addTexture(Multiproto.NAMESPACE.id("block/bricks")).index;
            Block.COBBLESTONE.textureId = terrain.addTexture(Multiproto.NAMESPACE.id("block/cobblestone")).index;
            slabSideTextures[0] = terrain.addTexture(Multiproto.NAMESPACE.id("block/smooth_stone_slab_side")).index;
            slabSideTextures[1] = terrain.addTexture(Multiproto.NAMESPACE.id("block/sandstone_slab_side")).index;
            slabSideTextures[2] = terrain.addTexture(Multiproto.NAMESPACE.id("block/planks_slab_side")).index;
            slabSideTextures[3] = terrain.addTexture(Multiproto.NAMESPACE.id("block/cobblestone_slab_side")).index;
        } else {
            Block.BRICKS.textureId = 7;
            Block.COBBLESTONE.textureId = 16;
        }
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9) && Multiproto.config.textureParity) {
            Block.REDSTONE_WIRE.textureId = redstoneWireTextures[0] = terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_cross")).index;
            terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_line"));
            redstoneWireTextures[1] = terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_cross_on")).index;
            terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_line_on"));
        } else {
            Block.REDSTONE_WIRE.textureId = 164;
        }
        Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (mc.worldRenderer != null) mc.worldRenderer.method_1537();
        Multiproto.LOGGER.info("Registered version parity textures");
    }
}
