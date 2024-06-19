package com.github.zr0n1.multiproto.protocol

import com.github.zr0n1.multiproto.protocol.Version.Type

/**
 * Represents a Minecraft multiplayer protocol version.
 */
class Version(
    /**
     * Protocol version number.
     * (Example: `14` for Beta 1.7 - Beta 1.7.3.)
     */
    @JvmField
    val protocol: Int,
    /**
     * Version type.
     *
     * @see Type
     */
    @JvmField
    val type: Type,
    /**
     * First client version number.
     */
    private val firstClient: String,
    /**
     * Last client version number.
     */
    private val lastClient: String = firstClient
) : Comparable<Version> {

    fun nameRange(shorten: Boolean = false): String {
        return if (firstClient == lastClient) name(firstClient, shorten)
        else name(firstClient, shorten) + (if (shorten) "-" else " - ") + name(lastClient, shorten)
    }

    fun name(shorten: Boolean = false) = name(lastClient, shorten)

    private fun name(s: String, shorten: Boolean): String {
        return type.getLabel(shorten) + (if (shorten) "" else " ") + (if (shorten) "" else type.prefix) + s
    }

    /**
     * @return [String] representing the [Version] by [.type] and [.protocol].<br></br>
     * (Example: [BETA_14] -> `"beta_14"`)
     */
    override fun toString(): String {
        return type.id + "_" + protocol
    }

    /**
     * Compares release order via type and version number.
     */
    override fun compareTo(other: Version): Int {
        if (this.type !== other.type) {
            if (VersionRegistry.TYPES.contains(type) && VersionRegistry.TYPES.contains(other.type)) {
                return VersionRegistry.TYPES.indexOf(type).compareTo(VersionRegistry.TYPES.indexOf(other.type))
            } else throw IllegalArgumentException("Version type: " + type.id + " is not registered!")
        }
        return this.protocol.compareTo(other.protocol)
    }

    @Deprecated("JAVA UTIL")
    fun isLE(other: Version) = this <= other
    @Deprecated("JAVA UTIL")
    fun isGE(other: Version) = this >= other

    data class Type(val id: String, private val label: String, private val abbreviation: String, val prefix: String = "") {
        fun getLabel(shorten: Boolean): String {
            return if (shorten) abbreviation else label
        }

        companion object {
            /**
             * Alpha v1.0.15 - Alpha v1.0.17_04
             */
            @JvmField
            val ALPHA: Type = Type("alpha", "Alpha", "a", "v")

            /**
             * Beta 1.0 - Beta 1.1_02
             */
            @JvmField
            val BETALPHA: Type = Type("betalpha", "Beta", "b")

            /**
             * Beta 1.2 - Beta 1.7.3
             */
            @JvmField
            val BETA: Type = Type("beta", "Beta", "b")
        }
    }

    companion object {
        /**
         * @param s [String] representing a protocol versions's type and version number.
         * @return [Version] which matches the given string or [.BETA_14].
         * @see Version.toString
         */
        @JvmStatic
        fun parse(s: String?): Version {
            if (s.isNullOrBlank()) return BETA_14
            val s1 = s.replace("\\s".toRegex(), "").replace("beta_initial", "betalpha")
            return VersionRegistry.VERSIONS.stream().filter { it.toString().equals(s1, ignoreCase = true) }
                .findFirst().orElse(BETA_14)
        }
    }
}


