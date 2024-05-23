package com.github.zr0n1.multiproto.protocol.packet.s2c.play;

import com.github.zr0n1.multiproto.protocol.packet.PacketHandler;
import net.minecraft.network.packet.s2c.play.LivingEntitySpawnS2CPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LivingEntitySpawnS2CPacketHandler extends PacketHandler<LivingEntitySpawnS2CPacket> {

    @Override
    public void read(LivingEntitySpawnS2CPacket packet, DataInputStream stream) throws IOException {
        packet.id = stream.readInt();
        packet.entityType = stream.readByte();
        packet.x = stream.readInt();
        packet.y = stream.readInt();
        packet.z = stream.readInt();
        packet.yaw = stream.readByte();
        packet.pitch = stream.readByte();
    }

    @Override
    public void write(LivingEntitySpawnS2CPacket packet, DataOutputStream stream) throws IOException {
        stream.writeInt(packet.id);
        stream.writeByte(packet.entityType);
        stream.writeInt(packet.x);
        stream.writeInt(packet.y);
        stream.writeInt(packet.z);
        stream.writeByte(packet.yaw);
        stream.writeByte(packet.pitch);
    }

    @Override
    public int size(LivingEntitySpawnS2CPacket packet) {
        return 19;
    }
}
