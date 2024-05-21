package com.github.zr0n1.multiproto;

import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import com.github.zr0n1.multiproto.parity.TextureParityHelper;
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
    void registerPlayerHandlers(PlayerEvent.HandlerRegister event) {
        if (event.player instanceof MultiplayerClientPlayerEntity player) {
            event.playerHandlers.add(new PlayerOnLadderHandler(event.player));
        }
    }

    @EventListener
    void registerVanillaRecipes(PacketRegisterEvent event) {
        RecipeParityHelper.vanillaCraftingRecipes = List.copyOf(CraftingRecipeManager.getInstance().getRecipes());
        RecipeParityHelper.vanillaSmeltingRecipes = Map.copyOf(SmeltingRecipeManager.getInstance().getRecipes());
    }

}
