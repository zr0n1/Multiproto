package com.github.zr0n1.multiproto.api;

import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EmptyPacketHandler<T extends Packet> extends PacketHandler<T> {

    @Override
    public void read(T packet, DataInputStream stream) throws IOException {
    }

    @Override
    public void write(T packet, DataOutputStream stream) throws IOException {
    }

    @Override
    public int size(T packet) {
        return 0;
    }
}
