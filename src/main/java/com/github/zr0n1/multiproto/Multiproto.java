package com.github.zr0n1.multiproto;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import com.github.zr0n1.multiproto.parity.player.PlayerOnLadderHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class Multiproto {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();
    @Entrypoint.Logger("multiproto")
    public static final Logger LOGGER = Null.get();
    @GConfig(value = "config", visibleName = "Multiproto Config")
    public static final Config config = new Config();

    @EventListener
    void registerTextures(TextureRegisterEvent event) {
        ExpandableAtlas terrain = Atlases.getTerrain();
        // old cobble and brick
        BlockParityHelper.cobblestoneTexture = terrain.addTexture(NAMESPACE.id("block/cobblestone")).index;
        BlockParityHelper.bricksTexture = terrain.addTexture(NAMESPACE.id("block/bricks")).index;
        // slab side textures
        BlockParityHelper.slabSideTextures = new int[] {
                terrain.addTexture(NAMESPACE.id("block/stone_slab_side")).index,
                terrain.addTexture(NAMESPACE.id("block/sandstone_slab_side")).index,
                terrain.addTexture(NAMESPACE.id("block/planks_slab_side")).index,
                terrain.addTexture(NAMESPACE.id("block/cobblestone_slab_side")).index
        };
        // redstone wire textures
        BlockParityHelper.redstoneWireTextures = new int[2];
        BlockParityHelper.redstoneWireTextures[0] = terrain.addTexture(NAMESPACE.id("block/redstone_wire_cross")).index;
        terrain.addTexture(NAMESPACE.id("block/redstone_wire_line"));
        BlockParityHelper.redstoneWireTextures[1] = terrain.addTexture(NAMESPACE.id("block/redstone_wire_cross_powered")).index;
        terrain.addTexture(NAMESPACE.id("block/redstone_wire_line_powered"));
        // apply parity
        BlockParityHelper.applyTextureParity();
        Minecraft mc = (Minecraft)FabricLoader.getInstance().getGameInstance();
        if(mc.worldRenderer != null) mc.worldRenderer.method_1537();
        LOGGER.info("Registered version parity textures");
    }

    @EventListener
    public void registerPlayerHandlers(PlayerEvent.HandlerRegister event) {
        if(event.player instanceof MultiplayerClientPlayerEntity player) {
            event.playerHandlers.add(new PlayerOnLadderHandler(event.player));
        }
    }

    @EventListener
    void registerVanillaRecipes(PacketRegisterEvent event) {
        RecipeParityHelper.vanillaCraftingRecipes = List.copyOf(CraftingRecipeManager.getInstance().getRecipes());
        RecipeParityHelper.vanillaSmeltingRecipes = Map.copyOf(SmeltingRecipeManager.getInstance().getRecipes());
    }

}
