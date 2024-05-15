package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.Multiproto;
import net.minecraft.block.Block;

public class VersionGraphicsHelper {
    public static int vanillaCobblestoneTexture;
    public static int vanillaBricksTexture;
    public static int cobblestoneTexture;
    public static int bricksTexture;

    public static void applyChanges() {
        ProtocolVersion v = ProtocolVersionManager.getCurrentVersion();
        if(Multiproto.config.versionGraphics && v.compareTo(ProtocolVersion.BETA_14) < 0) {
            Block.COBBLESTONE.textureId = cobblestoneTexture;
            Block.BRICKS.textureId = bricksTexture;
        } else {
            Block.COBBLESTONE.textureId = vanillaCobblestoneTexture;
            Block.BRICKS.textureId = vanillaBricksTexture;
        }
    }
}
