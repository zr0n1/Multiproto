package com.github.zr0n1.multiproto.mixinterface;

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
        return constructor(nbt.getString("name"), nbt.getString("ip"),
                ProtocolVersion.getVersionFromString(nbt.getString("version")));
    }
}
