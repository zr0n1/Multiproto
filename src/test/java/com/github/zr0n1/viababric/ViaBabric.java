package com.github.zr0n1.viababric;

import com.github.zr0n1.viababric.protocoltranslator.ViaBabricVLLoader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.raphimc.vialoader.ViaLoader;
import net.raphimc.vialoader.impl.platform.ViaLegacyPlatformImpl;
import org.apache.logging.log4j.Logger;
import pl.telvarost.mojangfixstationapi.Config;

@Environment(EnvType.CLIENT)
public class ViaBabric implements PreLaunchEntrypoint {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();
    @Entrypoint.Logger("ViaBabric")
    public static final Logger LOGGER = Null.get();

    @Override
    public void onPreLaunch() {
        ViaLoader.init(null, new ViaBabricVLLoader(), null, null, ViaLegacyPlatformImpl::new);
        LOGGER.info("bagoogity");
    }

    public static boolean shouldApplyMojangFixStationApiIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && Config.config.enableMultiplayerServerChanges;
    }
}
