package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class PacketHandler<T extends Packet> {

    protected static ItemStack readItemStack(DataInputStream stream) throws IOException {
        short id = stream.readShort();
        if (id >= 0) {
            byte count = stream.readByte();
            short damage = VersionManager.isBefore(Version.BETA_8) ? stream.readByte() : stream.readShort();
            return new ItemStack(id, count, damage);
        }
        return null;
    }

    protected static void writeItemStack(DataOutputStream stream, ItemStack stack) throws IOException {
        if (stack == null) {
            stream.writeShort(-1);
        } else {
            stream.writeShort(stack.itemId);
            stream.writeByte(stack.count);
            if (VersionManager.isBefore(Version.BETA_8)) stream.writeByte(stack.getDamage());
            else stream.writeShort(stack.getDamage());
        }
    }

    public void readPacket(Packet packet, DataInputStream stream) throws IOException {
        read((T)packet, stream);
    }

    public void writePacket(Packet packet, DataOutputStream stream) throws IOException {
        write((T)packet, stream);
    }

    public int packetSize(Packet packet) {
        return size((T)packet);
    }

    public void read(T packet, DataInputStream stream) throws IOException {
        packet.read(stream);
    }

    public void write(T packet, DataOutputStream stream) throws IOException {
        packet.write(stream);
    }

    public void apply(NetworkHandler handler, T packet) {
        packet.apply(handler);
    }

    public int size(T packet) {
        return packet.size();
    }
}
