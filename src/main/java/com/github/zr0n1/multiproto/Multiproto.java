package com.github.zr0n1.multiproto;
import com.github.zr0n1.multiproto.mixin.parity.hmifabric.UtilsAccessor;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.parity.ItemParityHelper;
import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import com.github.zr0n1.multiproto.protocol.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Multiproto {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();
    @Entrypoint.Logger("multiproto")
    public static final Logger LOGGER = Null.get();
    @GConfig(value = "config", visibleName = "Multiproto Config")
    public static final Config config = new Config();

    private static ProtocolVersion version = ProtocolVersion.BETA_14;

    @EventListener
    void registerTextures(TextureRegisterEvent event) {
        ExpandableAtlas terrain = Atlases.getTerrain();
        BlockParityHelper.cobblestoneTexture = terrain.addTexture(NAMESPACE.id("block/cobblestone")).index;
        BlockParityHelper.bricksTexture = terrain.addTexture(NAMESPACE.id("block/bricks")).index;
        BlockParityHelper.stoneSlabSideTexture = terrain.addTexture(NAMESPACE.id("block/stone_slab_side")).index;
        BlockParityHelper.sandstoneSlabSideTexture = terrain.addTexture(NAMESPACE.id("block/sandstone_slab_side")).index;
        BlockParityHelper.planksSlabSideTexture = terrain.addTexture(NAMESPACE.id("block/planks_slab_side")).index;
        BlockParityHelper.cobblestoneSlabSideTexture = terrain.addTexture(NAMESPACE.id("block/cobblestone_slab_side")).index;
        BlockParityHelper.visualParity();
        Minecraft mc = (Minecraft)FabricLoader.getInstance().getGameInstance();
        if(mc.worldRenderer != null) mc.worldRenderer.method_1537();
        LOGGER.info("Registered version parity textures");
    }

    @EventListener
    void registerVanillaRecipes(PacketRegisterEvent event) {
        RecipeParityHelper.vanillaCraftingRecipes = List.copyOf(CraftingRecipeManager.getInstance().getRecipes());
        RecipeParityHelper.vanillaSmeltingRecipes = Map.copyOf(SmeltingRecipeManager.getInstance().getRecipes());
    }

    public static ProtocolVersion getVersion() {
        return version;
    }

    public static void setVersion(ProtocolVersion version) {
        Multiproto.version = version;
        BlockParityHelper.parity();
        ItemParityHelper.parity();
        RecipeParityHelper.parity();
    }

    public static void loadLastVersion() {
        File file = new File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt");
        if(!file.exists()) return;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = br.readLine();
            br.close();
            setVersion(ProtocolVersion.fromString(s));
            LOGGER.info("Loaded last protocol version from text file");
        } catch(Exception e) {
            LOGGER.error("Unknown error loading last protocol version");
            e.printStackTrace();
        }
    }

    public static void saveLastVersion() {
        File file = new File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt");
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.print(version);
            pw.close();
        } catch(Exception e) {
            LOGGER.error("Error writing last protocol version to text file");
        }
    }

    public static boolean shouldApplyMojangFixStationApiIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && pl.telvarost.mojangfixstationapi.Config.config.enableMultiplayerServerChanges;
    }

    public static boolean shouldApplyHMIFabricIntegration() {
        return FabricLoader.getInstance().isModLoaded("hmifabric");
    }
}
