package com.github.zr0n1.multiproto.protocol.packet.s2c.play;

import com.github.zr0n1.multiproto.protocol.packet.PacketHandler;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EntityEquipmentUpdateS2CPacketHandler extends PacketHandler<EntityEquipmentUpdateS2CPacket> {

    @Override
    public void read(EntityEquipmentUpdateS2CPacket packet, DataInputStream stream) throws IOException {
        packet.id = stream.readInt();
        packet.slot = stream.readShort();
        packet.itemRawId = stream.readShort();
        packet.itemDamage = stream.readByte();
    }

    @Override
    public void write(EntityEquipmentUpdateS2CPacket packet, DataOutputStream stream) throws IOException {
        stream.writeInt(packet.id);
        stream.writeShort(packet.slot);
        stream.writeShort(packet.itemRawId);
        stream.writeByte(packet.itemDamage);
    }
}
