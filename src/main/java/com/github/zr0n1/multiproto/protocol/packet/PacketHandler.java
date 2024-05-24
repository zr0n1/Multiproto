package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.mixin.entity.client.ClientNetworkHandlerAccessor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class PacketHandler<T extends Packet> {

    protected ItemStack readItemStack(DataInputStream stream) throws IOException {
        short id = stream.readShort();
        if (id >= 0) {
            byte count = stream.readByte();
            short damage = VersionManager.isBefore(Version.BETA_8) ? stream.readByte() : stream.readShort();
            return new ItemStack(id, count, damage);
        }
        return null;
    }

    protected void writeItemStack(DataOutputStream stream, ItemStack stack) throws IOException {
        if (stack == null) {
            stream.writeShort(-1);
        } else {
            stream.writeShort(stack.itemId);
            stream.writeByte(stack.count);
            if (VersionManager.isBefore(Version.BETA_8)) stream.writeByte(stack.getDamage());
            else stream.writeShort(stack.getDamage());
        }
    }

    protected final Entity getEntity(int id, NetworkHandler handler) {
        return ((ClientNetworkHandlerAccessor) handler).invokeMethod_1645(id);
    }

    public final void readPacket(Packet packet, DataInputStream stream) throws IOException {
        read((T) packet, stream);
    }

    public final void writePacket(Packet packet, DataOutputStream stream) throws IOException {
        write((T) packet, stream);
    }

    public final int packetSize(Packet packet) {
        return size((T) packet);
    }

    public void read(T packet, DataInputStream stream) throws IOException {
        packet.read(stream);
    }

    public void write(T packet, DataOutputStream stream) throws IOException {
        packet.write(stream);
    }

    public int size(T packet) {
        return packet.size();
    }
}
