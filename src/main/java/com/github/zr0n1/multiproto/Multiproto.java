package com.github.zr0n1.multiproto;
import com.github.zr0n1.multiproto.protocol.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.recipe.CraftingRecipeManager;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Environment(EnvType.CLIENT)
public class Multiproto {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();
    @Entrypoint.Logger("multiproto")
    public static Logger LOGGER;
    @GConfig(value = "config", visibleName = "Multiproto Config")
    public static final Config config = new Config();

    @EventListener
    void registerTextures(TextureRegisterEvent event) {
        ExpandableAtlas terrain = Atlases.getTerrain();
        VersionGraphicsHelper.vanillaCobblestoneTexture = Block.COBBLESTONE.textureId;
        VersionGraphicsHelper.vanillaBricksTexture = Block.BRICKS.textureId;
        VersionGraphicsHelper.cobblestoneTexture = terrain.addTexture(NAMESPACE.id("block/cobblestone")).index;
        VersionGraphicsHelper.bricksTexture = terrain.addTexture(NAMESPACE.id("block/bricks")).index;
        VersionGraphicsHelper.applyChanges();
    }

    @EventListener
    void registerVanillaPacketsAndRecipes(PacketRegisterEvent event) {
        VersionCraftingHelper.vanillaRecipes = List.copyOf(CraftingRecipeManager.getInstance().getRecipes());
    }

    public static boolean shouldApplyMojangFixStationApiIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && pl.telvarost.mojangfixstationapi.Config.config.enableMultiplayerServerChanges;
    }
}
