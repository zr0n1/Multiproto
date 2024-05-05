package com.github.zr0n1.multiproto.protocol;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Minecraft multiplayer protocol version.
 */
public abstract class ProtocolVersion implements Comparable<ProtocolVersion> {

    public static final SortedSet<ProtocolVersion> PROTOCOL_VERSIONS = new TreeSet<>();
    public static final SortedSet<ProtocolVersion> ALPHA_PROTOCOL_VERSIONS = new TreeSet<>();
    public static final SortedSet<ProtocolVersion> BETA_PROTOCOL_VERSIONS = new TreeSet<>();

    /**
     * Beta 1.7 - Beta 1.7.3
     */
    public static final ProtocolVersion BETA_14 = new ProtocolVersion(14, Type.BETA,
            new VersionNames.Builder(Type.BETA, 7).subpatch(1).patchRange(2, 3).build()) {
    };
    /**
     * Beta 1.6 - Beta 1.6.6
     */
    public static final ProtocolVersion BETA_13 = new ProtocolVersion(13, Type.BETA,
            new VersionNames.Builder(Type.BETA,6).patchRange(0, 6).build()) {
    };

    /**
     * Beta 1.5 - Beta 1.5_02
     */
    public static final ProtocolVersion BETA_11 = new ProtocolVersion(11, Type.BETA,
            new VersionNames.Builder(Type.BETA, 5).subpatchRange(1, 2).build()) {
    };

    /**
     * Beta 1.4 - Beta 1.4_01
     */
    public static final ProtocolVersion BETA_10 = new ProtocolVersion(10, Type.BETA,
            new VersionNames.Builder(Type.BETA, 4).subpatch(1).build()) {
    };

    /**
     * Beta 1.3 - Beta 1.3_01
     */
    public static final ProtocolVersion BETA_9 = new ProtocolVersion(9, Type.BETA,
            new VersionNames.Builder(Type.BETA, 3).subpatch(1).build()) {
    };

    /**
     * Beta 1.1_02 - Beta 1.2_02
     */
    public static final ProtocolVersion BETA_8 = new ProtocolVersion(8, Type.BETA,
            new VersionNames.Builder(Type.BETA, 1, 0, 2).subpatchRange(2, 0, 0, 2).build()) {
    };

    /**
     * Beta 1.0 - Beta 1.1_01
     */
    public static final ProtocolVersion BETA_7 = new ProtocolVersion(7, Type.BETA,
            new VersionNames.Builder(Type.BETA, 0).subpatchRange(1, 2).add(1, 1).build()) {
    };

    /**
     * Alpha v1.2.3_05 - Alpha v1.2.6
     */
    public static final ProtocolVersion ALPHA_LATER_6 = new ProtocolVersion(6, Type.ALPHA_LATER,
            new VersionNames.Builder(Type.ALPHA_LATER, 2, 3, 5).add(4, 1).patchRange(4, 6).build()) {
    };

    /**
     * Alpha v1.2.3 - Alpha v1.2.3_04
     */
    public static final ProtocolVersion ALPHA_LATER_5 = new ProtocolVersion(5, Type.ALPHA_LATER,
            new VersionNames.Builder(Type.ALPHA_LATER, 2, 3).subpatchRange(3, 1, 2).add(3, 4).build()) {
    };

    /**
     * Alpha v1.2.2
     */
    public static final ProtocolVersion ALPHA_LATER_4 = new ProtocolVersion(4, Type.ALPHA_LATER,
            new VersionNames.Builder(Type.ALPHA_LATER, 2, 2).build()) {
    };

    /**
     * Alpha v1.2.0 - Alpha v1.2.1_01
     */
    public static final ProtocolVersion ALPHA_LATER_3 = new ProtocolVersion(3, Type.ALPHA_LATER,
            new VersionNames.Builder(Type.ALPHA_LATER, 2).subpatchRange(1, 2).subpatchRange(1, 0, 1).build()) {
    };

    /**
     * Alpha v1.1.0 - Alpha v1.1.2_01
     */
    public static final ProtocolVersion ALPHA_2 = new ProtocolVersion(2, Type.ALPHA,
            new VersionNames.Builder(Type.ALPHA, 1).patchRange(1, 2).add(2, 1).build()) {
    };

    /**
     * Alpha v1.0.17 - Alpha v1.0.17_04
     */
    public static final ProtocolVersion ALPHA_1 = new ProtocolVersion(1, Type.ALPHA,
            new VersionNames.Builder(Type.ALPHA, 0, 17).subpatchRange(17, 1, 4).build()) {
    };

    /**
     * Alpha v1.0.16 - Alpha v1.0.16_02
     */
    public static final ProtocolVersion ALPHA_EARLY_14 = new ProtocolVersion(14, Type.ALPHA_EARLY,
            new VersionNames.Builder(Type.ALPHA, 0, 16).subpatchRange(16, 1, 2).build()) {
    };

    /**
     * Alpha v1.0.15 (First version with SMP!)
     */
    public static final ProtocolVersion ALPHA_EARLY_13 = new ProtocolVersion(13, Type.ALPHA_EARLY,
            new VersionNames.Builder(Type.ALPHA, 0, 15).build()) {
    };


    private static ProtocolVersion currentVersion = BETA_14;

    public static ProtocolVersion getCurrentVersion() {
        return currentVersion;
    }

    public static void setCurrentVersion(ProtocolVersion currentVersion) {
        ProtocolVersion.currentVersion = currentVersion;
    }

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
     * List of version numbers from least to greatest.
     */
    public final VersionNames names;

    /**
     * @param version {@link #version}
     * @param type {@link #type}
     * @param names {@link #names}
     */
    public ProtocolVersion(int version, Type type, VersionNames names) {
        this.version = version;
        this.type = type;
        this.names = names;
        PROTOCOL_VERSIONS.add(this);
        if(type.alpha) {
            ALPHA_PROTOCOL_VERSIONS.add(this);
        } else {
            BETA_PROTOCOL_VERSIONS.add(this);
        }
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
        return PROTOCOL_VERSIONS.stream().filter(p ->
                        p.toString().equalsIgnoreCase(s1) || p.names.contains(s))
                .findFirst().orElse(BETA_14);
    }

    /**
     * Enum representing protocol type.
     */
    public enum Type {
        /**
         * Alpha versions before protocol numbering reset.
         */
        ALPHA_EARLY("Alpha", "a", true),
        /**
         * Alpha versions before v1.2.0.
         */
        ALPHA("Alpha", "a", true),
        /**
         * Alpha versions v1.2.0+.
         */
        ALPHA_LATER("Alpha", "a", true),
        /**
         * Beta versions.
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

    public static class VersionNames {

        private final Type type;
        private final SortedSet<VersionNumbers> numbers;

        private VersionNames(Type type, SortedSet<VersionNumbers> numbers) {
            this.type = type;
            this.numbers = numbers;
        }

        /**
         * {@link #range(boolean)}
         * @return Friendly version name range.<br>
         * (Example: {@code "Beta 1.7 - Beta 1.7.3"})
         */
        public String range() {
            return range(false);
        }

        /**
         * @param abbreviate Whether to use short version names.<br>
         * (Example: {@code "b1.7-b1.7.3"})
         * @return Friendly version name range.
         * <br>(Example: {@code "Beta 1.7 - Beta 1.7.3"})
         * @see #first(boolean)
         * @see #last(boolean)
         */
        public String range(boolean abbreviate) {
            return numbers.size() > 1 ?
                    String.join(abbreviate ? "-" : " - ", first(abbreviate), last(abbreviate)) :
                    first(abbreviate);
        }

        /**
         * {@link #first(boolean)}
         * @return First ordered friendly version name.<br>
         * (Example: {@code "Beta 1.7"})
         * @see #last()
         */
        public String first() {
            return first(false);
        }

        /**
         * @param abbreviate Whether to use the short version name.<br>
         * (Example: {@code "b1.7"} instead of {@code "Beta 1.7"}
         * @return First ordered friendly version name.<br>
         * (Example: {@code "Beta 1.7"})
         * @see #last(boolean)
         */
        public String first(boolean abbreviate) {
            return name(numbers.first(), abbreviate);
        }

        /**
         * {@link #last(boolean)}
         * @return Last ordered friendly version name.<br>
         * (Example: {@code "Beta 1.7"})
         * @see #first()
         */
        public String last() {
            return last(false);
        }

        /**
         * @param abbreviate Whether to use the short version name.<br>
         * (Example: {@code "b1.7.3"} instead of {@code "Beta 1.7.3"}
         * @return Last ordered friendly version name.<br>
         * (Example: {@code "Beta 1.7"})
         * @see #first(boolean)
         */
        public String last(boolean abbreviate) {
            return name(numbers.last(), abbreviate);
        }

        public List<String> names(boolean abbreviate) {
            List<String> list = new ArrayList<>();
            numbers.forEach(n -> list.add(name(n, abbreviate)));
            return list;
        }

        private String name(VersionNumbers n, boolean abbreviate) {
            String s = n.major + "." + n.minor + (n.patch > 0 || type.alpha ? "." + n.patch : "");
            if(n.subpatch > 0) s += String.format("_%02d", n.subpatch);
            return (abbreviate ? type.shortLabel : (type.label + (type.alpha ? " v" : " "))) + s;
        }

        /**
         * @param s Version name.
         * @return Whether the version name is contained within the set of version names.
         */
        public boolean contains(String s) {
            VersionNumbers n = match(s);
            return n != null && numbers.contains(n);
        }

        private VersionNumbers match(String s) {
            Matcher matcher = Pattern.compile
                            ("(?<maj>\\d+)\\.(?<min>\\d+)(?:\\.(?<pat>\\d+))?(?:_0?(?<sub>\\d+))?")
                    .matcher(s);
            if(!matcher.matches()) return null;
            int maj = Integer.parseInt(matcher.group("maj"));
            int min = Integer.parseInt(matcher.group("min"));
            int pat = (matcher.group("pat") != null) ? Integer.parseInt(matcher.group("pat")) : 0;
            int sub = (matcher.group("sub") != null) ? Integer.parseInt(matcher.group("sub")) : 0;
            return new VersionNumbers(maj, min, pat, sub);
        }

        public static class Builder {
            private final Type type;
            private final int major;
            private final int startingMinor;
            private final SortedSet<VersionNumbers> numbers;

            public Builder(Type type, int minor) {
                this(type, type.majorVersion, minor, 0, 0);
            }

            public Builder(Type type, int minor, int patch) {
                this(type, type.majorVersion, minor, patch, 0);
            }

            public Builder(Type type, int minor, int patch, int subpatch) {
                this(type, type.majorVersion, minor, patch, subpatch);
            }

            public Builder(Type type, int major, int minor, int patch, int subpatch) {
                this.type = type;
                this.major = major;
                this.startingMinor = minor;
                this.numbers = new TreeSet<>();
                add(minor, patch, subpatch);
            }

            /**
             * @return {@link VersionNames} instance.
             */
            public VersionNames build() {
                return new VersionNames(type, numbers);
            }

            public Builder patchRange(int start, int end) {
                return patchRange(startingMinor, start, end);
            }

            /**
             * Adds patch version numbers inside a range.
             * @param minor Minor version number.
             * @param start First patch version number to add.
             * @param end Last patch version number to add.
             * @return Itself.
             */
            public Builder patchRange(int minor, int start, int end) {
                for(int patch = start; patch <= end; patch++) add(minor, patch, 0);
                return this;
            }

            public Builder subpatchRange(int start, int end) {
                return subpatchRange(startingMinor, 0, start, end);
            }

            public Builder subpatchRange(int patch, int start, int end) {
                return subpatchRange(startingMinor, patch, start, end);
            }

            public Builder subpatchRange(int minor, int patch, int start, int end) {
                for(int subpatch = start; subpatch <= end; subpatch++) add(minor, patch, subpatch);
                return this;
            }

            public Builder subpatch(int i) {
                return add(startingMinor, 0, i);
            }

            public Builder add(int patch, int subpatch) {
                return add(startingMinor, patch, subpatch);
            }

            /**
             * Adds a version.
             * @param minor Minor version number.
             * @param patch Patch number.
             * @param subpatch Subpatch version number.
             * @return Itself.
             */
            public Builder add(int minor, int patch, int subpatch) {
                numbers.add(new VersionNumbers(major, minor, patch, subpatch));
                return this;
            }

        }
        private record VersionNumbers(int major, int minor, int patch, int subpatch) implements Comparable<VersionNumbers> {
            /**
             * Compares by major, minor, patch, then subpatch version.
             */
            @Override
            public int compareTo(@NotNull ProtocolVersion.VersionNames.VersionNumbers v) {
                return (this.major != v.major) ? Integer.compare(this.major, v.minor) :
                        (this.minor != v.minor) ? Integer.compare(this.minor, v.minor) :
                        (this.patch != v.patch) ? Integer.compare(this.patch, v.patch) :
                        (this.subpatch != v.subpatch) ? Integer.compare(this.subpatch, v.subpatch) : 0;

            }
        }
    }
}


