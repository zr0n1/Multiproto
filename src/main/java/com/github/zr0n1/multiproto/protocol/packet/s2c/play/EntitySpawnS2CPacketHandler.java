package com.github.zr0n1.multiproto.protocol.packet.s2c.play;

import com.github.zr0n1.multiproto.api.PacketHandler;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EntitySpawnS2CPacketHandler extends PacketHandler<EntitySpawnS2CPacket> {

    @Override
    public void read(EntitySpawnS2CPacket packet, DataInputStream stream) throws IOException {
        packet.id = stream.readInt();
        packet.entityType = stream.readByte();
        packet.x = stream.readInt();
        packet.y = stream.readInt();
        packet.z = stream.readInt();
    }

    @Override
    public void write(EntitySpawnS2CPacket packet, DataOutputStream stream) throws IOException {
        stream.writeInt(packet.id);
        stream.writeByte(packet.entityType);
        stream.writeInt(packet.x);
        stream.writeInt(packet.y);
        stream.writeInt(packet.z);
    }

    @Override
    public int size(EntitySpawnS2CPacket packet) {
        return 17;
    }
}
