package com.github.zr0n1.multiproto.mixinterface;

import com.github.zr0n1.multiproto.protocol.Version;
import net.minecraft.nbt.NbtCompound;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

public interface MultiprotoServerData {
    Version multiproto_getVersion();
    void multiproto_setVersion(Version version);

    static ServerData create(NbtCompound nbt) {
        return create(nbt.getString("name"), nbt.getString("ip"), Version.parse(nbt.getString("version")));
    }

    @SuppressWarnings("all")
    static ServerData create(String name, String ip, Version version) {
        ServerData data = new ServerData(name, ip);
        ((MultiprotoServerData) data).multiproto_setVersion(version);
        return data;
    }
}
