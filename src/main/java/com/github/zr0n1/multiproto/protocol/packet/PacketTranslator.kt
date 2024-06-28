package com.github.zr0n1.multiproto.protocol.packet

import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.protocol.*
import net.minecraft.network.packet.Packet
import com.github.zr0n1.multiproto.api.packet.PacketWrapper
import net.minecraft.network.NetworkHandler

@Suppress("unchecked_cast")
object PacketTranslator : VersionChangedListener {
    private val WRAPPERS: MutableMap<Int, (Packet) -> PacketWrapper<out Packet>> = HashMap()
    private val C2S_REDIRECTS: MutableMap<Int, (Packet) -> Packet> = HashMap()
    private val REPLACEMENTS: MutableMap<Int, () -> Packet> = HashMap()
    private val APPLIERS: MutableMap<Int, (Packet, NetworkHandler) -> Unit> = HashMap()

    @JvmStatic
    fun wrap(packet: Packet): Packet? = WRAPPERS[packet.rawId]?.invoke(packet).also { it?.wrapperId = packet.rawId }

    @JvmStatic
    fun redirect(packet: Packet) = C2S_REDIRECTS[packet.rawId]?.invoke(packet)

    @JvmStatic
    fun replace(id: Int) = REPLACEMENTS[id]?.invoke()

    @JvmStatic
    fun apply(packet: Packet, handler: NetworkHandler) = APPLIERS[packet.rawId]?.invoke(packet, handler)

    @JvmStatic
    fun hasWrapper(id: Int) = id in WRAPPERS

    @JvmStatic
    fun hasRedirect(id: Int) = C2S_REDIRECTS.containsKey(id)

    @JvmStatic
    fun isReplaced(id: Int) = REPLACEMENTS.containsKey(id)

    @JvmStatic
    fun hasApplier(id: Int) = id in APPLIERS

    override fun invoke() {
        WRAPPERS.clear()
        C2S_REDIRECTS.clear()
        REPLACEMENTS.clear()
        APPLIERS.clear()
        BetaPackets.invoke()
        BetalphaPackets.invoke()
        AlphaPackets.invoke()
    }

    @JvmStatic
    fun <T : Packet> wrapLE(target: Version, id: Int, wrapper: (T) -> PacketWrapper<T>) {
        if (currVer <= target) WRAPPERS[id] = wrapper as (Packet) -> PacketWrapper<out Packet>
    }

    @JvmStatic
    @Suppress("unused")
    fun <T : Packet> wrapFrom(min: Version, max: Version, id: Int, wrapper: (T) -> PacketWrapper<out Packet>) {
        if (currVer in min..max) WRAPPERS[id] = wrapper as (Packet) -> PacketWrapper<out Packet>
    }

    @JvmStatic
    fun <T : Packet> redirectLE(target: Version, id: Int, redirect: (T) -> Packet) {
        if (currVer <= target) C2S_REDIRECTS[id] = redirect as (Packet) -> Packet
    }

    @JvmStatic
    fun replaceLE(target: Version, id: Int, replacement: () -> Packet) {
        if (currVer <= target) REPLACEMENTS[id] = replacement
    }

    @JvmStatic
    fun <T : Packet> applyLE(target: Version, id: Int, applier: (T, NetworkHandler) -> Unit) {
        if (currVer <= target) APPLIERS[id] = applier as (Packet, NetworkHandler) -> Unit
    }
}
