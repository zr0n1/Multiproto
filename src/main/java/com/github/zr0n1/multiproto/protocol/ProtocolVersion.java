package com.github.zr0n1.multiproto.protocol;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents a Minecraft multiplayer protocol version.
 */
public class ProtocolVersion implements Comparable<ProtocolVersion> {
    public static final SortedSet<ProtocolVersion> PROTOCOL_VERSIONS = new TreeSet<>();
    public static final SortedSet<ProtocolVersion> ALPHA_PROTOCOL_VERSIONS = new TreeSet<>();
    public static final SortedSet<ProtocolVersion> BETA_PROTOCOL_VERSIONS = new TreeSet<>();

    /**
     * Beta 1.7 - Beta 1.7.3
     */
    public static final ProtocolVersion BETA_14 = new ProtocolVersion(14, Type.BETA, "1.7", "1.7.3");
    /**
     * Beta 1.6 - Beta 1.6.6
     */
    public static final ProtocolVersion BETA_13 = new ProtocolVersion(13, Type.BETA, "1.6", "1.6.6");

    /**
     * Beta 1.5 - Beta 1.5_01
     */
    public static final ProtocolVersion BETA_11 = new ProtocolVersion(11, Type.BETA, "1.5", "1.5_01");

    /**
     * Beta 1.4 - Beta 1.4_01
     */
    public static final ProtocolVersion BETA_10 = new ProtocolVersion(10, Type.BETA, "1.4", "1.4_01");

    /**
     * Beta 1.3 - Beta 1.3_01
     */
    public static final ProtocolVersion BETA_9 = new ProtocolVersion(9, Type.BETA, "1.3", "1.3_01");

    /**
     * Beta 1.2 - Beta 1.2_02
     */
    public static final ProtocolVersion BETA_8 = new ProtocolVersion(8, Type.BETA, "1.2", "1.2_02");

    /**
     * Beta 1.1_02
     */
    public static final ProtocolVersion BETA_INITIAL_8 = new ProtocolVersion(8, Type.BETA_INITIAL, "1.1_02");

    /**
     * Beta 1.0 - Beta 1.1_01
     */
    public static final ProtocolVersion BETA_INITIAL_7 = new ProtocolVersion(7, Type.BETA_INITIAL, "1.0", "1.1_01");

    /**
     * Alpha v1.2.3_05 - Alpha v1.2.6
     */
    public static final ProtocolVersion ALPHA_LATER_6 = new ProtocolVersion(6, Type.ALPHA_LATER,
            "1.2.3_05", "1.2.6");

    /**
     * Alpha v1.2.3 - Alpha v1.2.3_04
     */
    public static final ProtocolVersion ALPHA_LATER_5 = new ProtocolVersion(5, Type.ALPHA_LATER,
            "1.2.3", "1.2.3_04");

    /**
     * Alpha v1.2.2
     */
    public static final ProtocolVersion ALPHA_LATER_4 = new ProtocolVersion(4, Type.ALPHA_LATER,
            "1.2.2");

    /**
     * Alpha v1.2.0 - Alpha v1.2.1_01
     */
    public static final ProtocolVersion ALPHA_LATER_3 = new ProtocolVersion(3, Type.ALPHA_LATER,
            "1.20", "1.2.1_01");

    /**
     * Alpha v1.1.0 - Alpha v1.1.2_01
     */
    public static final ProtocolVersion ALPHA_2 = new ProtocolVersion(2, Type.ALPHA,
            "1.1.0", "1.1.2_01");

    /**
     * Alpha v1.0.17 - Alpha v1.0.17_04
     */
    public static final ProtocolVersion ALPHA_1 = new ProtocolVersion(1, Type.ALPHA,
            "1.0.17", "1.0.17_04");

    /**
     * Alpha v1.0.16 - Alpha v1.0.16_02
     */
    public static final ProtocolVersion ALPHA_INITIAL_14 = new ProtocolVersion(14, Type.ALPHA_INITIAL, "1.0.16",
            "1.0.16_02");

    /**
     * Alpha v1.0.15 (First version with SMP!)
     */
    public static final ProtocolVersion ALPHA_INITIAL_13 = new ProtocolVersion(13, Type.ALPHA_INITIAL, "1.0.15");

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
     * First client version number.
     */
    public final String firstClient;
    /**
     * Last client version number.
     */
    public final String lastClient;

    /**
     * @param version {@link #version}
     * @param type {@link #type}
     * @param client {@link #firstClient} {@link #lastClient}
     */

    public ProtocolVersion(int version, Type type, String client) {
        this(version, type, client, client);
    }

    public ProtocolVersion(int version, Type type, String firstClient, String lastClient) {
        this.version = version;
        this.type = type;
        this.firstClient = firstClient;
        this.lastClient = lastClient;
        PROTOCOL_VERSIONS.add(this);
        if(type.alpha) ALPHA_PROTOCOL_VERSIONS.add(this);
        else BETA_PROTOCOL_VERSIONS.add(this);
    }

    public String nameRange() {
        return nameRange(false);
    }

    public String nameRange(boolean abbreviate) {
        return (firstClient.equals(lastClient) ? name(firstClient, abbreviate) :
                String.join((abbreviate ? "-" : " - "), name(firstClient, abbreviate), name(lastClient, abbreviate)));
    }

    public String name() {
        return name(lastClient, false);
    }

    public String name(boolean abbreviate) {
        return name(lastClient, abbreviate);
    }

    private String name(String s, boolean abbreviate) {
        return (abbreviate ? type.shortLabel : type.label) + (abbreviate ? "" : " ") + (type.alpha && !abbreviate ? "v" : "") + s;
    }

    /**
     * @return {@link String} representing the {@link ProtocolVersion} by {@link #type} and {@link #version}.<br>
     * (Example: {@link #BETA_14} -> {@code "beta_14"})
     */
    @Override
    public String toString() {
        return String.join("_", type.toString(), String.valueOf(version)).toLowerCase();
    }

    /**
     * Compares by sequential protocol order.
     */
    @Override
    public int compareTo(@NotNull ProtocolVersion v) {
        if(this.type != v.type) return this.type.compareTo(v.type);
        return Integer.compare(this.version, v.version);
    }

    /**
     * @param s {@link String} representing a protocol's type and version.
     * @return {@link ProtocolVersion} which matches the given string or {@link #BETA_14}.
     * @see #toString()
     */
    public static ProtocolVersion getVersionFromString(String s) {
        String s1 = s.replaceAll("\\s", "");
        return PROTOCOL_VERSIONS.stream().filter(p -> p.toString().equalsIgnoreCase(s1)).findFirst().orElse(BETA_14);
    }

    /**
     * Enum representing protocol type.
     */
    public enum Type {
        /**
         * Alpha v1.0.15 - Alpha v1.0.16_02.
         */
        ALPHA_INITIAL("Alpha", "a", true),
        /**
         * Alpha v1.0.17 - Alpha v1.1.2_01.
         */
        ALPHA("Alpha", "a", true),
        /**
         * Alpha v1.2.0 - v1.2.6.
         */
        ALPHA_LATER("Alpha", "a", true),
        /**
         * Beta 1.0 - Beta 1.1_02.
         */
        BETA_INITIAL("Beta", "b"),
        /**
         * Beta 1.2 - Beta 1.7.3.
         */
        BETA("Beta", "b");

        public final String label;
        public final String shortLabel;
        public final int majorVersion;
        public final boolean alpha;

        Type(String label, String shortLabel, boolean alpha) {
            this.label = label;
            this.shortLabel = shortLabel;
            this.majorVersion = 1;
            this.alpha = alpha;
        }

        Type(String label, String shortLabel) {
            this.label = label;
            this.shortLabel = shortLabel;
            this.majorVersion = 1;
            this.alpha = false;
        }
    }
}


