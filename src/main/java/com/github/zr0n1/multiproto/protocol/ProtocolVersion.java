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

    /**
     * Beta 1.7 - Beta 1.7.3
     */
    public static final ProtocolVersion BETA_14 = new ProtocolVersion(14, Type.BETA,
            new VersionNames.Builder(Type.BETA, 7, 0, 3).put(1, 1).build()) {
    };
    /**
     * Beta 1.6 - Beta 1.6.6
     */
    public static final ProtocolVersion BETA_13 = new ProtocolVersion(13, Type.BETA,
            new VersionNames.Builder(Type.BETA,6, 0, 6).build()) {
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
            private final int defaultMinor;
            private final SortedSet<VersionNumbers> numbers;

            public Builder(Type type, int minor, int start, int end) {
                this(type, type.majorVersion, minor, start, end);
            }

            public Builder(Type type, int major, int minor, int start, int end) {
                this.type = type;
                this.major = major;
                this.defaultMinor = minor;
                this.numbers = new TreeSet<>();
                patchRange(minor, start, end);
            }

            /**
             * @return {@link VersionNames} instance.
             */
            public VersionNames build() {
                return new VersionNames(type, numbers);
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

            public Builder subpatchRange(int minor, int patch, int start, int end) {
                for(int subpatch = start; subpatch <= end; subpatch++) add(minor, patch, subpatch);
                return this;
            }

            public Builder add(int patch, int subpatch) {
                return add(defaultMinor, patch, subpatch);
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

            /**
             * Removes a patch version.
             * @param minor Minor version number.
             * @param patch Patch version number.
             * @return Itself.
             */
            private Builder remove(int minor, int patch) {
                numbers.remove(new VersionNumbers(major, minor, patch, 0));
                return this;
            }

            /**
             * Replaces a patch version with a subpatch version.<br>
             * Used for subpatch versions which have no corresponding patch version within the protocol version
             * (a1.2.3_05, a1.2.4_01) or for versions with an x.y_z subpatch but no x.y.z patch (b1.7_01).
             * @param patch Patch version to replace.
             * @param subpatch Replacement subpatch version.
             * @return Itself.
             */
            public Builder put(int patch, int subpatch) {
                return put(defaultMinor, patch, 0, subpatch);
            }

            /**
             * Replaces a patch version with a subpatch version.<br>
             * Used for subpatch versions which have no corresponding patch version within the protocol version
             * (a1.2.3_05, a1.2.4_01) or for versions with an x.y_z subpatch but no x.y.z patch (b1.7_01).
             * @param minor Minor version to replace a patch for.
             * @param patch Patch version to replace.
             * @param subpatch Replacement subpatch version.
             * @return Itself.
             */
            public Builder put(int minor, int patch, int subpatch) {
                return put(minor, patch, 0, subpatch);
            }

            /**
             * Replaces a patch version with a subpatch version.<br>
             * Used for subpatch versions which have no corresponding patch version within the protocol version
             * (a1.2.3_05, a1.2.4_01) or for x.y versions with a x.y_0z subpatch but no x.y.z patch (b1.7_01).
             * @param minor Minor version to replace a patch for.
             * @param patch Patch version to replace.
             * @param replacement Replacement patch version.
             * @param subpatch Replacement subpatch version.
             * @return Itself.
             */
            public Builder put(int minor, int patch, int subpatch, int replacement) {
                return remove(minor, patch).add(minor, replacement, subpatch);
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


