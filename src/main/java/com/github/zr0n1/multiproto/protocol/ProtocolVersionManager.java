package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.parity.ItemParityHelper;
import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import com.github.zr0n1.multiproto.parity.TextureParityHelper;
import net.minecraft.client.Minecraft;

import java.io.*;

public final class ProtocolVersionManager {

    private static ProtocolVersion version = ProtocolVersion.BETA_14;
    private static ProtocolVersion lastVersion;

    public static ProtocolVersion getVersion() {
        return version;
    }

    public static void setVersion(ProtocolVersion version) {
        if (ProtocolVersionManager.version != version) {
            ProtocolVersionManager.version = version;
            BlockParityHelper.parity();
            ItemParityHelper.parity();
            RecipeParityHelper.parity();
            TextureParityHelper.parity();
        }
    }

    public static ProtocolVersion getLastVersion() {
        if (lastVersion == null) {
            File file = new File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt");
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
            File file = new File(Minecraft.getRunDirectory(), "config/multiproto/lastversion.txt");
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file));
                pw.print(ProtocolVersionManager.lastVersion = lastVersion);
                pw.close();
            } catch (Exception e) {
                Multiproto.LOGGER.error("Error writing last protocol version to text file");
            }
        }
    }
}
