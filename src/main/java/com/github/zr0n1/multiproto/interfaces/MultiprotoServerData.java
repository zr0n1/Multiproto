package com.github.zr0n1.multiproto.interfaces;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.nbt.NbtCompound;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

public interface MultiprotoServerData {
    ProtocolVersion getVersion();
    void setVersion(ProtocolVersion version);

    static ServerData constructor(String name, String ip, ProtocolVersion protocol) {
        ServerData data = new ServerData(name, ip);
        ((MultiprotoServerData)data).setVersion(protocol);
        return data;
    }

    static ServerData constructor(NbtCompound nbt) {
        ServerData data = new ServerData(nbt.getString("name"), nbt.getString("ip"));
        ((MultiprotoServerData)data).setVersion(ProtocolVersion.getVersionFromString(nbt.getString("version")));
        return data;
    }
}
