package com.github.zr0n1.multiproto;

import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.parity.ItemParityHelper;
import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.*;

public final class Utils {

    private static ProtocolVersion version = ProtocolVersion.BETA_14;

    public static ProtocolVersion getVersion() {
        return version;
    }

    public static void setVersion(ProtocolVersion version) {
        if(Utils.version == version) return;
        Utils.version = version;
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
        } catch(Exception e) {
            Multiproto.LOGGER.error("Unknown error loading last protocol version");
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
            Multiproto.LOGGER.error("Error writing last protocol version to text file");
        }
    }

    public static boolean shouldApplyMojangFixStAPIServerListIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && pl.telvarost.mojangfixstationapi.Config.config.enableMultiplayerServerChanges;
    }

    public static boolean shouldApplyMojangFixStAPIDebugScreenIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && pl.telvarost.mojangfixstationapi.Config.config.enableDebugMenuWorldSeed;
    }

    public static boolean shouldApplyHMIFabricIntegration() {
        return FabricLoader.getInstance().isModLoaded("hmifabric");
    }
}
