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
    public static final Version BETALPHA_8 = new Version(8, Type.BETALPHA, "1.1_02");

    /**
     * Beta 1.0 - Beta 1.1_01
     */
    public static final Version BETALPHA_7 = new Version(7, Type.BETALPHA, "1.0", "1.1_01");

    /**
     * Alpha v1.2.3_05 - Alpha v1.2.6
     */
    public static final Version ALPHAWEEN_6 = new Version(6, Type.ALPHAWEEN,
            "1.2.3_05", "1.2.6");

    /**
     * Alpha v1.2.3 - Alpha v1.2.3_04
     */
    public static final Version ALPHAWEEN_5 = new Version(5, Type.ALPHAWEEN,
            "1.2.3", "1.2.3_04");

    /**
     * Alpha v1.2.2
     */
    public static final Version ALPHAWEEN_4 = new Version(4, Type.ALPHAWEEN,
            "1.2.2");

    /**
     * Alpha v1.2.0 - Alpha v1.2.1_01
     */
    public static final Version ALPHAWEEN_3 = new Version(3, Type.ALPHAWEEN,
            "1.20", "1.2.1_01");

    /**
     * Alpha v1.1.1 - Alpha v1.1.2_01
     */
    public static final Version ALPHA_2 = new Version(2, Type.ALPHA,
            "1.1.1", "1.1.2_01");

    /**
     * Protocol version number.
     * (Example: {@code 14} for Beta 1.7 - Beta 1.7.3.)
     */
    public final int protocol;
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


    public Version(int protocol, Type type, String client) {
        this(protocol, type, client, client);
    }

    public Version(int protocol, Type type, String firstClient, String lastClient) {
        this.protocol = protocol;
        this.type = type;
        this.firstClient = firstClient;
        this.lastClient = lastClient;
    }

    /**
     * @param s {@link String} representing a protocol versions's type and version number.
     * @return {@link Version} which matches the given string or {@link #BETA_14}.
     * @see #toString()
     */
    public static Version parse(String s) {
        if (s == null || s.isBlank()) return BETA_14;
        String s1 = s.replaceAll("\\s", "").replace("beta_initial", "betalpha");
        return Versions.VERSIONS.stream().filter(p -> p.toString().equalsIgnoreCase(s1)).findFirst().orElse(BETA_14);
    }

    public String nameRange(boolean abbreviate) {
        return (firstClient.equals(lastClient) ? name(firstClient, abbreviate) :
                String.join((abbreviate ? "-" : " - "), name(firstClient, abbreviate), name(lastClient, abbreviate)));
    }

    public String name(boolean abbreviate) {
        return name(lastClient, abbreviate);
    }

    private String name(String s, boolean abbreviate) {
        return type.getLabel(abbreviate) + (abbreviate ? "" : " ") + type.prefix + s;
    }

    /**
     * @return {@link String} representing the {@link Version} by {@link #type} and {@link #protocol}.<br>
     * (Example: {@link #BETA_14} -> {@code "beta_14"})
     */
    @Override
    public String toString() {
        return type.id + "_" + protocol;
    }

    /**
     * Compares release order via type and version number.
     */
    @Override
    public int compareTo(Version version) {
        if (this.type != version.type) {
            if (Versions.TYPES.contains(type) && Versions.TYPES.contains(version.type)) {
                return Integer.compare(Versions.TYPES.indexOf(type), Versions.TYPES.indexOf(version.type));
            } else throw new IllegalArgumentException("Version type: " + type.id + " is not registered!");
        }
        return Integer.compare(this.protocol, version.protocol);
    }

    public record Type(String id, String label, String abbreviation, String prefix) {

        /**
         * Alpha v1.0.15 - Alpha v1.0.17_04
         */
        public static final Type ALPHA = new Type("alpha", "Alpha", "a", "v");
        public static final Type ALPHAWEEN = new Type("alphaween", "Alpha", "a", "v");
        public static final Type BETALPHA = new Type("betalpha", "Beta", "b");
        public static final Type BETA = new Type("beta", "Beta", "b");

        public Type(String id, String label, String abbreviation) {
            this(id, label, abbreviation, "");
        }

        public String getLabel(boolean abbreviated) {
            return abbreviated ? abbreviation : label;
        }
    }
}


