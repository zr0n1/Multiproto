package com.github.zr0n1.multiproto.protocol

import com.github.zr0n1.multiproto.api.event.RegisterVersionsListener
import com.github.zr0n1.multiproto.util.fabric
import com.google.common.collect.ImmutableSortedSet

object VersionRegistry {
    internal lateinit var TYPES: List<Version.Type>
    internal lateinit var VERSIONS: ImmutableSortedSet<Version>

    private val _types = arrayListOf(
        Version.Type.ALPHA,
        Version.Type.BETALPHA,
        Version.Type.BETA
    )

    private val _versions = arrayListOf(
        BETA_14,
        BETA_13,
        BETA_11,
        BETA_10,
        BETA_9,
        BETA_8,
        BETALPHA_8,
        BETALPHA_7,
        ALPHA_6,
        ALPHA_5,
        ALPHA_4,
        ALPHA_3,
        ALPHA_2
    )

    @JvmSynthetic
    internal fun registerAll() {
        fabric.invokeEntrypoints("multiproto:register_versions", RegisterVersionsListener::class.java) {
            it.registerTypes()
            it.registerVersions()
        }
        TYPES = _types.toList()
        VERSIONS = ImmutableSortedSet.copyOf(_versions)
    }

    @JvmStatic
    @Suppress("unused")
    fun register(vararg versions: Version) {
        versions.forEach { require(TYPES.contains(it.type)) { "Type ${it.type.id} for version $it must be registered!"} }
        _versions.addAll(versions)
    }

    @JvmStatic
    @Suppress("unused")
    fun registerTypeBefore(target: Version.Type, type: Version.Type) {
        _types.add(_types.indexOf(target), type)
    }

    @JvmStatic
    @Suppress("unused")
    fun registerTypeAfter(target: Version.Type, type: Version.Type) {
        _types.add(_types.indexOf(target) + 1, type)
    }
}