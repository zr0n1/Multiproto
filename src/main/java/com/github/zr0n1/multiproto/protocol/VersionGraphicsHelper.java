package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.Multiproto;
import net.minecraft.block.Block;

public class VersionGraphicsHelper {
    public static int cobblestoneTexture;
    public static int bricksTexture;
    public static int sandstoneSlabSideTexture;
    public static int stoneSlabSideTexture;
    public static int planksSlabSideTexture;
    public static int cobblestoneSlabSideTexture;

    public static void applyChanges() {
        ProtocolVersion v = ProtocolVersionManager.getCurrentVersion();
        if(Multiproto.config.versionGraphics && v.compareTo(ProtocolVersion.BETA_14) < 0) {
            Block.COBBLESTONE.textureId = cobblestoneTexture;
            Block.BRICKS.textureId = bricksTexture;
        } else {
            Block.COBBLESTONE.textureId = 16;
            Block.BRICKS.textureId = 7;
        }
    }
}
