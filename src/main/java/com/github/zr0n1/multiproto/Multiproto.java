package com.github.zr0n1.multiproto;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.telvarost.mojangfixstationapi.Config;

@Environment(EnvType.CLIENT)
public class Multiproto implements ClientModInitializer {

    public static Logger LOGGER;
    public static ModMetadata METADATA;

    @Override
    public void onInitializeClient() {
        ModContainer container =
                FabricLoader.getInstance().getModContainer("multiproto").orElseThrow(NullPointerException::new);
        METADATA = container.getMetadata();
        LOGGER = LogManager.getLogger(METADATA.getName());
    }

    public static boolean shouldApplyMojangFixStationApiIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && Config.config.enableMultiplayerServerChanges;
    }
}
