package com.github.zr0n1.multiproto.protocol;

public class ProtocolVersionManager {

    public ProtocolVersionManager() {
        this.currentVersion = ProtocolVersion.BETA_14;
    }

    public ProtocolVersion getCurrentProtocol() {
        return currentVersion;
    }

    public void setCurrentProtocol(ProtocolVersion p) {
        this.currentVersion = p;
    }

    private ProtocolVersion currentVersion;
}
