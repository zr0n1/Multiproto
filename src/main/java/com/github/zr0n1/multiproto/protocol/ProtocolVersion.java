package com.github.zr0n1.multiproto.protocol;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Represents a Minecraft multiplayer protocol version.
 */
public abstract class ProtocolVersion {

    /**
     * Protocol version int.
     * (Example: {@code 14} for Beta 1.7 - Beta 1.7.3.)
     */
    public final int version;
    /**
     * Version type.
     * @see Type
     */
    public final Type type;
    /**
     * First version number.
     * (Example: {@code "1.7"})
     */
    public final String firstVersion;
    /**
     * Last version number.
     * (Example: {@code "1.7.3"})
     */
    public final String lastVersion;

    /**
     *
     * @param version {@link #version}
     * @param type {@link #type}
     * @param first {@link #firstVersion}
     * @param last {@link #lastVersion}
     */
    public ProtocolVersion(int version, Type type, String first, String last) {
        this.version = version;
        this.type = type;
        this.firstVersion = first;
        this.lastVersion = last;
    }

    /**
     *
     * @return Friendly protocol version name.
     * <br>(Example: {@link #BETA_14} -> {@code "Beta 1.7 - Beta 1.7.3"})
     */
    public String versionName() {
        return WordUtils.capitalizeFully(
                new StringJoiner(" - ", type + " ", "")
                .add(firstVersion)
                .add(lastVersion)
                .toString());
    }

    /**
     *
     * @return Friendly first version name.
     * <br>(Example: {@link #BETA_14} -> {@code "Beta 1.7"})
     */
    public String firstVersionName() {
        return WordUtils.capitalizeFully(type + " " + firstVersion);
    }

    /**
     *
     * @return Friendly last version name.
     * <br>(Example: {@link #BETA_14} -> {@code "Beta 1.7.3"})
     */
    public String lastVersionName() {
        return WordUtils.capitalizeFully(type + " " + lastVersion);
    }

    /**
     *
     * @return {@link String} representing the {@link ProtocolVersion} by {@link #type} and {@link #version}.
     * <br>Example: {@link #BETA_14} -> {@code "beta_14"}
     */
    @Override
    public String toString() {
        return String.join("_", type.toString(), String.valueOf(version)).toLowerCase();
    }

    /**
     * Beta 1.7 - Beta 1.7.3
     */
    public static final ProtocolVersion BETA_14 = new ProtocolVersion(14, Type.BETA,"1.7" , "1.7.3") {
    };
    /**
     * Beta 1.6 - Beta 1.6.6
     */
    public static final ProtocolVersion BETA_13 = new ProtocolVersion(13, Type.BETA, "1.6", "1.6.6") {
    };

    public static final Set<ProtocolVersion> PROTOCOL_VERSIONS = new LinkedHashSet<>();
    public static final Set<ProtocolVersion> CLASSIC_PROTOCOL_VERSIONS = new LinkedHashSet<>();
    public static final Set<ProtocolVersion> ALPHA_PROTOCOL_VERSIONS = new LinkedHashSet<>();
    public static final Set<ProtocolVersion> BETA_PROTOCOL_VERSIONS = new LinkedHashSet<>();

    /**
     * @param s {@link String} representing a protocol's type and version.
     * @return {@link ProtocolVersion} which matches the given string or {@link #BETA_14}.
     * @see #toString()
     * @see #versionName()
     * @see #firstVersionName()
     * @see #lastVersionName()
     */
    public static ProtocolVersion getProtocolFromString(String s) {
        String s1 = s.replaceAll("\\s", "");
        return PROTOCOL_VERSIONS.stream().filter(p ->
                p.toString().equalsIgnoreCase(s1) || p.versionName().equalsIgnoreCase(s1) ||
                    p.firstVersionName().equalsIgnoreCase(s1) || p.lastVersionName().equalsIgnoreCase(s1))
                        .findFirst().orElse(BETA_14);
    }

    /**
     * Registers protocols.
     * @param protocolVersions One or more protocols.
     */
    public static void registerProtocols(ProtocolVersion... protocolVersions) {
        PROTOCOL_VERSIONS.addAll(Arrays.stream(protocolVersions).toList());
        CLASSIC_PROTOCOL_VERSIONS.addAll(Arrays.stream(protocolVersions).filter(p -> p.type == Type.CLASSIC).toList());
        ALPHA_PROTOCOL_VERSIONS.addAll(Arrays.stream(protocolVersions).filter(p -> p.type == Type.ALPHA).toList());
        BETA_PROTOCOL_VERSIONS.addAll(Arrays.stream(protocolVersions).filter(p -> p.type == Type.CLASSIC).toList());
    }

    static {
        registerProtocols(BETA_14, BETA_13);
    }

    /**
     * Enum representing protocol type.
     */
    public enum Type {
        CLASSIC,
        ALPHA,
        BETA;
    }
}


