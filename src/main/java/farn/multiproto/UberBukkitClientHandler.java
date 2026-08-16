package farn.multiproto;

import farn.multiproto.packet.BlockBreakingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public class UberBukkitClientHandler {

    public static HashMap<BlockPos, BlockBreakingPacket> mpDigging = new HashMap<>();

    public static void render(Minecraft mc) {
        if (!mpDigging.isEmpty()) {
            ArrayList<BlockPos> col = new ArrayList<>(mpDigging.keySet());
            for (BlockPos key : col) {
                BlockBreakingPacket packet = mpDigging.get(key);

                if (packet.timestamp + 1000L < System.currentTimeMillis() || mc.world.getBlockId(packet.x, packet.y, packet.z) == 0 || mc.player.getSquaredDistance(packet.x, packet.y, packet.z) > 64D) {
                    mpDigging.remove(key);
                    continue;
                }

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                HitResult mop = new HitResult(packet.x, packet.y, packet.z, packet.face, null);
                mc.worldRenderer.renderBlockOutline(mc.player, mop, 0, null, packet.progress);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }
        }
    }

}
