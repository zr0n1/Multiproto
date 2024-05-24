package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.mixin.MultiprotoMixinPlugin;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.parity.HMIFabricIntegrationHelper;
import com.github.zr0n1.multiproto.parity.ItemParityHelper;
import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import com.github.zr0n1.multiproto.parity.optional.TextureParityHelper;
import com.github.zr0n1.multiproto.parity.optional.TranslationParityHelper;
import com.github.zr0n1.multiproto.protocol.event.VersionChangedListener;
import com.github.zr0n1.multiproto.protocol.packet.PacketHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

import java.io.*;

public final class VersionManager {

    private static Version version = Version.BETA_14;
    private static Version lastVersion;

    public static Version getVersion() {
        return version;
    }

    public static void setVersion(Version version) {
        if (VersionManager.version != version) {
            VersionManager.version = version;
            PacketHelper.register();
            BlockParityHelper.applyParity();
            ItemParityHelper.applyParity();
            RecipeParityHelper.applyParity();
            TextureParityHelper.applyParity();
            TranslationParityHelper.applyParity();
            if (MultiprotoMixinPlugin.shouldApplyHMIFabricIntegration()) HMIFabricIntegrationHelper.applyParity();
            FabricLoader.getInstance().getEntrypointContainers("multiproto:version_changed", VersionChangedListener.class)
                    .forEach(listener -> listener.getEntrypoint().onVersionChanged());

        }
    }

    public static boolean isBefore(Version target) {
        return version.isBefore(target);
    }

    public static Version getLastVersion() {
        if (lastVersion == null) {
            File file = new File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt");
            if (file.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String s = br.readLine();
                    br.close();
                    lastVersion = Version.fromString(s);
                } catch (Exception e) {
                    Multiproto.LOGGER.error("Error loading last protocol version");
                    e.printStackTrace();
                    lastVersion = Version.BETA_14;
                }
            } else lastVersion = Version.BETA_14;
        }
        return lastVersion;
    }

    public static void setLastVersion(Version lastVersion) {
        if (VersionManager.lastVersion != lastVersion) {
            File file = new File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt");
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file));
                pw.print(VersionManager.lastVersion = lastVersion);
                pw.close();
            } catch (Exception e) {
                Multiproto.LOGGER.error("Error writing last protocol version to text file");
                e.printStackTrace();
            }
        }
    }
}
