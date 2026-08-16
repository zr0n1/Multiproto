package farn.multiproto.packet;

import com.github.zr0n1.multiproto.Multiproto;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerSoundPacket extends Packet {
	public String sound;
	public double locX;
	public double locY;
	public double locZ;
	public float volume;
	public float pitch;

	public void read(DataInputStream var1) {
		try {
			this.sound = var1.readUTF();
			this.locX = var1.readDouble();
			this.locY = var1.readDouble();
			this.locZ = var1.readDouble();
			this.volume = var1.readFloat();
			this.pitch = var1.readFloat();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(DataOutputStream var1) {
	}

	public void apply(NetworkHandler var1) {
		if(Multiproto.config.play62Sound)
			PlayerHelper.getPlayerFromGame().world.playSound(locX, locY, locZ, sound, volume, pitch);
	}

	public int size() {
		return this.sound.length() + 24 + 8;
	}
}
