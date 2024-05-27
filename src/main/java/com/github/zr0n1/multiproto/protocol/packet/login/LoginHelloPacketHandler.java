package com.github.zr0n1.multiproto.protocol.packet.login;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.api.PacketHandler;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.login.LoginHelloPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginHelloPacketHandler extends PacketHandler<LoginHelloPacket> {

    public String password = "Password";

    @Override
    public void read(LoginHelloPacket packet, DataInputStream stream) throws IOException {
        packet.protocolVersion = stream.readInt();
        packet.username = Packet.readString(stream, 16);
        if (VersionManager.isBefore(Version.BETA_11)) password = stream.readUTF();
        packet.worldSeed = stream.readLong();
        packet.dimensionId = stream.readByte();
    }

    @Override
    public void write(LoginHelloPacket packet, DataOutputStream stream) throws IOException {
        Multiproto.LOGGER.info("Logging in as {} with protocol version {}", packet.username, VersionManager.getVersion().protocol);
        stream.writeInt(packet.protocolVersion = VersionManager.getVersion().protocol);
        Packet.writeString(packet.username, stream);
        if (VersionManager.isBefore(Version.BETA_11)) stream.writeUTF(password);
        stream.writeLong(packet.worldSeed);
        stream.writeByte(packet.dimensionId);
    }

    @Override
    public int size(LoginHelloPacket packet) {
        return packet.size() + (VersionManager.isBefore(Version.BETA_11) ? password.length() : 0);
    }
}
