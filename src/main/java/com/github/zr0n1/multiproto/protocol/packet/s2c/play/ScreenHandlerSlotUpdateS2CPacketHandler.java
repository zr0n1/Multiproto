package com.github.zr0n1.multiproto.protocol.packet.s2c.play;

import com.github.zr0n1.multiproto.api.PacketHandler;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ScreenHandlerSlotUpdateS2CPacketHandler extends PacketHandler<ScreenHandlerSlotUpdateS2CPacket> {

    @Override
    public void read(ScreenHandlerSlotUpdateS2CPacket packet, DataInputStream stream) throws IOException {
        packet.syncId = stream.readByte();
        packet.slot = stream.readShort();
        packet.stack = readItemStack(stream);
    }

    @Override
    public void write(ScreenHandlerSlotUpdateS2CPacket packet, DataOutputStream stream) throws IOException {
        stream.writeByte(packet.syncId);
        stream.writeShort(packet.slot);
        writeItemStack(stream, packet.stack);
    }

    @Override
    public int size(ScreenHandlerSlotUpdateS2CPacket packet) {
        return 7;
    }
}
