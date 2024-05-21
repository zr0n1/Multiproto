package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.parity.ItemParityHelper;
import com.github.zr0n1.multiproto.parity.RecipeParityHelper;
import net.minecraft.client.Minecraft;

import java.io.*;

public final class ProtocolVersionManager {

    private static ProtocolVersion version = ProtocolVersion.BETA_14;

    public static ProtocolVersion getVersion() {
        return version;
    }

    public static void setVersion(ProtocolVersion version) {
        if(ProtocolVersionManager.version == version) return;
        ProtocolVersionManager.version = version;
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
}
