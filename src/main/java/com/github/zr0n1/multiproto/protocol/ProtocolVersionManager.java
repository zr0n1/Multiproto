package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.parity.ItemParityHelper;
import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import com.github.zr0n1.multiproto.parity.optional.TextureParityHelper;
import com.github.zr0n1.multiproto.parity.optional.TranslationParityHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.*;

public final class ProtocolVersionManager {

    private static ProtocolVersion version = ProtocolVersion.BETA_14;
    private static ProtocolVersion lastVersion;
    private static File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "multiproto");

    public static ProtocolVersion getVersion() {
        return version;
    }

    public static void setVersion(ProtocolVersion version) {
        if (ProtocolVersionManager.version != version) {
            ProtocolVersionManager.version = version;
            BlockParityHelper.applyParity();
            ItemParityHelper.applyParity();
            RecipeParityHelper.applyParity();
            TextureParityHelper.applyParity();
            TranslationParityHelper.applyParity();
        }
    }

    public static boolean isBefore(ProtocolVersion target) {
        return version.isBefore(target);
    }

    public static boolean isBeforeOrBukkitClient(ProtocolVersion version) {
        return isBefore(version) || (getVersion().isBukkitClient());
    }

    public static ProtocolVersion getLastVersion() {
        if (lastVersion == null) {
            File file = new File(configDir, "lastversion.txt");
            if (file.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String s = br.readLine();
                    br.close();
                    lastVersion = ProtocolVersion.fromString(s);
                } catch (Exception e) {
                    Multiproto.LOGGER.error("Unknown error loading last protocol version");
                    e.printStackTrace();
                    lastVersion = ProtocolVersion.BETA_14;
                }
            } else lastVersion = ProtocolVersion.BETA_14;
        }
        return lastVersion;
    }

    public static void setLastVersion(ProtocolVersion lastVersion) {
        if (ProtocolVersionManager.lastVersion != lastVersion) {
            File file = new File(configDir, "lastversion.txt");
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file));
                pw.print(ProtocolVersionManager.lastVersion = lastVersion);
                pw.close();
            } catch (Exception e) {
                Multiproto.LOGGER.error("Error writing last protocol version to text file");
                e.printStackTrace();
            }
        }
    }

    static {
        configDir.mkdirs();
    }
}
