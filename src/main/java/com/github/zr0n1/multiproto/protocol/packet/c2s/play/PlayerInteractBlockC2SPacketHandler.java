package com.github.zr0n1.multiproto.protocol.packet.c2s.play;

import com.github.zr0n1.multiproto.api.PacketHandler;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerInteractBlockC2SPacketHandler extends PacketHandler<PlayerInteractBlockC2SPacket> {

    @Override
    public void read(PlayerInteractBlockC2SPacket packet, DataInputStream stream) throws IOException {
        packet.x = stream.readInt();
        packet.y = stream.read();
        packet.z = stream.readInt();
        packet.side = stream.read();
        packet.stack = readItemStack(stream);
    }

    @Override
    public void write(PlayerInteractBlockC2SPacket packet, DataOutputStream stream) throws IOException {
        stream.writeInt(packet.x);
        stream.write(packet.y);
        stream.writeInt(packet.z);
        stream.write(packet.side);
        writeItemStack(stream, packet.stack);
    }

    @Override
    public int size(PlayerInteractBlockC2SPacket packet) {
        return 14;
    }
}
