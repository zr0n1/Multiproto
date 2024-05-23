package com.github.zr0n1.multiproto.protocol.packet;

import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EmptyPacketHandler extends PacketHandler<Packet> {

    @Override
    public void readPacket(Packet packet, DataInputStream stream) throws IOException {
    }

    @Override
    public void write(Packet packet, DataOutputStream stream) throws IOException {
    }

    @Override
    public int size(Packet packet) {
        return 0;
    }
}
