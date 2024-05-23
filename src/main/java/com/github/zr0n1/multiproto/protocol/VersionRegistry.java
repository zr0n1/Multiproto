package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.protocol.event.RegisterVersionsListener;
import com.google.common.collect.ImmutableSortedSet;
import net.fabricmc.loader.api.FabricLoader;

import java.util.*;

public final class VersionRegistry {

    public static SortedSet<Version> VERSIONS;

    public static void registerVersions() {
        if(VERSIONS != null) throw new AssertionError("no");
        ImmutableSortedSet.Builder<Version> builder = ImmutableSortedSet.naturalOrder();
        builder.add(
                Version.BETA_14,
                Version.BETA_13,
                Version.BETA_11,
                Version.BETA_10,
                Version.BETA_9,
                Version.BETA_8,
                Version.BETA_INITIAL_8,
                Version.BETA_INITIAL_7
        );
        FabricLoader.getInstance().getEntrypointContainers("multiproto:register_versions", RegisterVersionsListener.class)
                .forEach(listener -> builder.add(listener.getEntrypoint().register()));
        VERSIONS = builder.build();
    }
}
