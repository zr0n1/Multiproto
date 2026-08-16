package farn.multiproto.packet;

import com.github.zr0n1.multiproto.Multiproto;
import farn.multiproto.UberBukkitClientHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.BlockPos;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class BlockBreakingPacket extends Packet {
	public int x;
	public int y;
	public int z;
	public int face;
	public float progress;
	public long timestamp;

	public void read(DataInputStream var1) {
		try {
			this.x = var1.readInt();
			this.y = var1.readInt();
			this.z = var1.readInt();
			this.face = var1.readByte();
			this.progress = var1.readFloat();
			this.timestamp = System.currentTimeMillis();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void write(DataOutputStream var1) {
	}

	public void apply(NetworkHandler var1) {
		if(!Multiproto.config.blockBreaking) return;

		Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
		mc.particleManager.addBlockBreakingParticles(x, y, z, face);

		BlockPos cc = new BlockPos(x, y, z);

		if (UberBukkitClientHandler.mpDigging.containsKey(cc)) {
			UberBukkitClientHandler.mpDigging.remove(cc, this);
		}

		if (progress != 0F) {
			UberBukkitClientHandler.mpDigging.put(cc, this);
		}

	}

	public int size() {
		return 17;
	}
}
