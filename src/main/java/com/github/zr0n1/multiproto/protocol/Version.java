package com.github.zr0n1.multiproto.protocol;

/**
 * Represents a Minecraft multiplayer protocol version.
 */
public class Version implements Comparable<Version> {

    /**
     * Beta 1.7 - Beta 1.7.3
     */
    public static final Version BETA_14 = new Version(14, Type.BETA, "1.7", "1.7.3");
    /**
     * Beta 1.6 - Beta 1.6.6
     */
    public static final Version BETA_13 = new Version(13, Type.BETA, "1.6", "1.6.6");

    /**
     * Beta 1.5 - Beta 1.5_01
     */
    public static final Version BETA_11 = new Version(11, Type.BETA, "1.5", "1.5_01");

    /**
     * Beta 1.4 - Beta 1.4_01
     */
    public static final Version BETA_10 = new Version(10, Type.BETA, "1.4", "1.4_01");

    /**
     * Beta 1.3 - Beta 1.3_01
     */
    public static final Version BETA_9 = new Version(9, Type.BETA, "1.3", "1.3_01");

    /**
     * Beta 1.2 - Beta 1.2_02
     */
    public static final Version BETA_8 = new Version(8, Type.BETA, "1.2", "1.2_02");

    /**
     * Beta 1.1_02
     */
    public static final Version BETA_INITIAL_8 = new Version(8, Type.BETA_INITIAL, "1.1_02");

    /**
     * Beta 1.0 - Beta 1.1_01
     */
    public static final Version BETA_INITIAL_7 = new Version(7, Type.BETA_INITIAL, "1.0", "1.1_01");

    /**
     * Alpha v1.2.3_05 - Alpha v1.2.6
     */
    public static final Version ALPHA_LATER_6 = new Version(6, Type.ALPHA_LATER,
            "1.2.3_05", "1.2.6");

    /**
     * Alpha v1.2.3 - Alpha v1.2.3_04
     */
    public static final Version ALPHA_LATER_5 = new Version(5, Type.ALPHA_LATER,
            "1.2.3", "1.2.3_04");

    /**
     * Alpha v1.2.2
     */
    public static final Version ALPHA_LATER_4 = new Version(4, Type.ALPHA_LATER,
            "1.2.2");

    /**
     * Alpha v1.2.0 - Alpha v1.2.1_01
     */
    public static final Version ALPHA_LATER_3 = new Version(3, Type.ALPHA_LATER,
            "1.20", "1.2.1_01");

    /**
     * Alpha v1.1.0 - Alpha v1.1.2_01
     */
    public static final Version ALPHA_2 = new Version(2, Type.ALPHA,
            "1.1.0", "1.1.2_01");

    /**
     * Alpha v1.0.17 - Alpha v1.0.17_04
     */
    public static final Version ALPHA_1 = new Version(1, Type.ALPHA,
            "1.0.17", "1.0.17_04");

    /**
     * Alpha v1.0.16 - Alpha v1.0.16_02
     */
    public static final Version ALPHA_INITIAL_14 = new Version(14, Type.ALPHA_INITIAL, "1.0.16",
            "1.0.16_02");

    /**
     * Alpha v1.0.15 (First version with SMP!)
     */
    public static final Version ALPHA_INITIAL_13 = new Version(13, Type.ALPHA_INITIAL, "1.0.15");

    /**
     * Protocol version number.
     * (Example: {@code 14} for Beta 1.7 - Beta 1.7.3.)
     */
    public final int version;
    /**
     * Version type.
     *
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
     * @param type    {@link #type}
     * @param client  {@link #firstClient} {@link #lastClient}
     */

    public Version(int version, Type type, String client) {
        this(version, type, client, client);
    }

    public Version(int version, Type type, String firstClient, String lastClient) {
        this.version = version;
        this.type = type;
        this.firstClient = firstClient;
        this.lastClient = lastClient;
    }

    /**
     * @param s {@link String} representing a protocol versions's type and version number.
     * @return {@link Version} which matches the given string or {@link #BETA_14}.
     * @see #toString()
     */
    public static Version fromString(String s) {
        if (s == null || s.isBlank()) return BETA_14;
        String s1 = s.replaceAll("\\s", "");
        return VersionRegistry.VERSIONS.stream().filter(p -> p.toString().equalsIgnoreCase(s1)).findFirst().orElse(BETA_14);
    }

    public String nameRange(boolean abbreviate) {
        return (firstClient.equals(lastClient) ? name(firstClient, abbreviate) :
                String.join((abbreviate ? "-" : " - "), name(firstClient, abbreviate), name(lastClient, abbreviate)));
    }

    public String name(boolean abbreviate) {
        return name(lastClient, abbreviate);
    }

    private String name(String s, boolean abbreviate) {
        return type.getLabel(abbreviate) + (abbreviate ? "" : " ") + (type.alpha && !abbreviate ? "v" : "") + s;
    }

    /**
     * @return {@link String} representing the {@link Version} by {@link #type} and {@link #version}.<br>
     * (Example: {@link #BETA_14} -> {@code "beta_14"})
     */
    @Override
    public String toString() {
        return String.join("_", type.toString(), String.valueOf(version)).toLowerCase();
    }

    public boolean isBefore(Version target) {
        return this.compareTo(target) < 0;
    }

    /**
     * Compares release order via type and version number.
     */
    @Override
    public int compareTo(Version version) {
        if (this.type != version.type) return this.type.compareTo(version.type);
        return Integer.compare(this.version, version.version);
    }

    /**
     * Enum representing protocol version type.
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
        BETA_INITIAL("Beta", "b", false),
        /**
         * Beta 1.2 - Beta 1.7.3.
         */
        BETA("Beta", "b", false);

        private final String label;
        private final String abbreviated;
        public final boolean alpha;

        Type(String label, String abbreviated, boolean alpha) {
            this.label = label;
            this.abbreviated = abbreviated;
            this.alpha = alpha;
        }

        public String getLabel(boolean abbreviate) {
            return abbreviate ? abbreviated : label;
        }
    }
}


