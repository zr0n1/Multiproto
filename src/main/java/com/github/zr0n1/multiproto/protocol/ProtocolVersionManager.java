package com.github.zr0n1.multiproto.protocol;

public class ProtocolVersionManager {

    private static ProtocolVersion currentVersion = ProtocolVersion.BETA_14;

    public static ProtocolVersion getCurrentVersion() {
        return currentVersion;
    }

    public static void setCurrentVersion(ProtocolVersion version) {
        currentVersion = version;
        VersionRecipesHelper.applyChanges();
        VersionItemHelper.applyChanges();
        VersionGraphicsHelper.applyChanges();
    }
}
