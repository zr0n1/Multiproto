package com.github.zr0n1.multiproto.mixinterface;

import com.github.zr0n1.multiproto.protocol.Version;

public interface MultiprotoEditServerScreen {
    Version multiproto_getVersion();
    void multiproto_setVersion(Version ver);
}
