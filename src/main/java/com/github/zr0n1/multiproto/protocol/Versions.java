package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.api.event.RegisterVersionsListener;
import net.fabricmc.loader.api.FabricLoader;

import java.util.*;

public final class Versions {

    public static List<Version.Type> TYPES = new ArrayList<>(Arrays.asList(
            Version.Type.ALPHA,
            Version.Type.ALPHAWEEN,
            Version.Type.BETALPHA,
            Version.Type.BETA
    ));

    public static SortedSet<Version> VERSIONS = new TreeSet<>(Arrays.asList(
            Version.BETA_14,
            Version.BETA_13,
            Version.BETA_11,
            Version.BETA_10,
            Version.BETA_9,
            Version.BETA_8,
            Version.BETALPHA_8,
            Version.BETALPHA_7
    ));

    public static void registerVersions() {
        FabricLoader.getInstance().getEntrypointContainers("multiproto:register_versions", RegisterVersionsListener.class)
                .forEach(container -> container.getEntrypoint().register());
        TYPES = Collections.unmodifiableList(TYPES);
        VERSIONS = Collections.unmodifiableSortedSet(VERSIONS);
    }

    public static void register(Version... versions) {
        for (Version version : versions) {
            if (!TYPES.contains(version.type)) {
                throw new IllegalArgumentException("Version type must be registered!");
            }
        }
        VERSIONS.addAll(Arrays.asList(versions));
    }

    public static void registerTypeBefore(Version.Type target, Version.Type type) {
        TYPES.add(TYPES.indexOf(target), type);
    }

    public static void registerTypeAfter(Version.Type target, Version.Type type) {
        TYPES.add(TYPES.indexOf(target) + 1, type);
    }
}
