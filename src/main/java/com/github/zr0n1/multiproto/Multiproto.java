package com.github.zr0n1.multiproto;

import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import farn.multiproto.packet.BlockBreakingPacket;
import farn.multiproto.packet.ServerSoundPacket;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class Multiproto {

    @Entrypoint.Namespace
    public static Namespace NAMESPACE = Null.get();
    @Entrypoint.Logger("Multiproto")
    public static Logger LOGGER = Null.get();
    @ConfigRoot(value = "config", visibleName = "Multiproto Config")
    public static Config config = new Config();

    @EventListener
    public void registerVanillaRecipes(PacketRegisterEvent event) {
        event.register(62, true, false, ServerSoundPacket.class);
        event.register(63, true, false, BlockBreakingPacket.class);
        RecipeParityHelper.vanillaCraftingRecipes = List.copyOf(CraftingRecipeManager.getInstance().getRecipes());
        RecipeParityHelper.vanillaSmeltingRecipes = Map.copyOf(SmeltingRecipeManager.getInstance().getRecipes());
    }

}
