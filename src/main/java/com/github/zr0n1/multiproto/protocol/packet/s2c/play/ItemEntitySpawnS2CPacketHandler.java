package com.github.zr0n1.multiproto.protocol.packet.s2c.play;

import com.github.zr0n1.multiproto.protocol.packet.PacketHandler;
import net.minecraft.network.packet.s2c.play.ItemEntitySpawnS2CPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ItemEntitySpawnS2CPacketHandler extends PacketHandler<ItemEntitySpawnS2CPacket> {

    @Override
    public void read(ItemEntitySpawnS2CPacket packet, DataInputStream stream) throws IOException {
        packet.id = stream.readInt();
        packet.itemRawId = stream.readShort();
        packet.itemCount = stream.readByte();
        packet.x = stream.readInt();
        packet.y = stream.readInt();
        packet.z = stream.readInt();
        packet.velocityX = stream.readByte();
        packet.velocityY = stream.readByte();
        packet.velocityZ = stream.readByte();
    }

    @Override
    public void write(ItemEntitySpawnS2CPacket packet, DataOutputStream stream) throws IOException {
        stream.writeInt(packet.id);
        stream.writeShort(packet.itemRawId);
        stream.writeByte(packet.itemCount);
        stream.writeInt(packet.x);
        stream.writeInt(packet.y);
        stream.writeInt(packet.z);
        stream.writeByte(packet.velocityX);
        stream.writeByte(packet.velocityY);
        stream.writeByte(packet.velocityZ);
    }

    @Override
    public int size(ItemEntitySpawnS2CPacket packet) {
        return 22;
    }
}
