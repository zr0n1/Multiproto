package com.github.zr0n1.multiproto.protocol.packet.c2s.play;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.github.zr0n1.multiproto.protocol.packet.PacketHandler;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClickSlotC2SPacketHandler extends PacketHandler<ClickSlotC2SPacket> {

    @Override
    public void read(ClickSlotC2SPacket packet, DataInputStream stream) throws IOException {
        packet.syncId = stream.readByte();
        packet.slot = stream.readShort();
        packet.button = stream.readByte();
        packet.actionType = stream.readShort();
        packet.holdingShift = !VersionManager.isBefore(Version.BETA_11) && stream.readBoolean();
        packet.stack = readItemStack(stream);
    }

    @Override
    public void write(ClickSlotC2SPacket packet, DataOutputStream stream) throws IOException {
        stream.writeByte(packet.syncId);
        stream.writeShort(packet.slot);
        stream.writeByte(packet.button);
        stream.writeShort(packet.actionType);
        if (!VersionManager.isBefore(Version.BETA_11)) stream.writeBoolean(packet.holdingShift);
        writeItemStack(stream, packet.stack);
    }

    @Override
    public int size(ClickSlotC2SPacket packet) {
        return VersionManager.isBefore(Version.BETA_8) ? 10 : super.size(packet);
    }
}
