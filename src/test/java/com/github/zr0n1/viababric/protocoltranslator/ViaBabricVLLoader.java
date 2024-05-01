package com.github.zr0n1.viababric.protocoltranslator;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import net.raphimc.vialegacy.api.LegacyProtocolVersion;
import net.raphimc.vialoader.impl.viaversion.VLLoader;

public class ViaBabricVLLoader extends VLLoader {

    @Override
    public void load() {
        super.load();
        Via.getManager().getProviders().use(VersionProvider.class, new VersionProvider() {
            @Override
            public ProtocolVersion getClosestServerProtocol(UserConnection connection) {
                return LegacyProtocolVersion.b1_7tob1_7_3;
            }
        });
    }
}
