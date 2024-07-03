package com.github.zr0n1.multiproto.protocol

import com.github.zr0n1.multiproto.event.VersionChangedEvent
import com.github.zr0n1.multiproto.mixin.network.PacketAccessor
import com.github.zr0n1.multiproto.parity.VersionParity
import com.github.zr0n1.multiproto.protocol.packet.PacketWrapper
import net.minecraft.network.NetworkHandler
import net.minecraft.network.packet.Packet
import net.modificationstation.stationapi.api.StationAPI

@Suppress("unchecked_cast")
object Protocol {
    /**
     * Current protocol version.
     */
    @JvmStatic
    @get:JvmName("getVer")
    var version: Version = Version.B1_7_3
        @JvmName("setVer")
        set(value) {
            if (field != value) {
                field = value
                reset()
                field.packets()
                VersionParity.apply()
                StationAPI.EVENT_BUS.post(VersionChangedEvent())
            }
        }

    @JvmStatic
    @Deprecated("JAVA UTIL", ReplaceWith("Protocol.version LE other"))
    fun verLE(other: Version) = this.version <= other

    @JvmStatic
    @Deprecated("JAVA UTIL", ReplaceWith("Protocol.version GE other"))
    fun verGE(other: Version) = this.version >= other

    private val VANILLA_CLIENTBOUND_IDS: Set<Int> by lazy { PacketAccessor.getClientBoundPackets().toMutableSet() }
    private val VANILLA_SERVERBOUND_IDS: Set<Int> by lazy { PacketAccessor.getServerBoundPackets().toMutableSet() }
    private val replacements: MutableMap<Int, () -> Packet> = HashMap()
    private val redirects: MutableMap<Int, (Packet) -> Packet> = HashMap()
    private val wrappers: MutableMap<Int, (Packet) -> PacketWrapper<out Packet>> = HashMap()
    private val handlers: MutableMap<Int, (Packet, NetworkHandler) -> Unit> = HashMap()
    private lateinit var clientBoundIds: MutableSet<Int>
    private lateinit var serverBoundIds: MutableSet<Int>

    @JvmStatic
    fun translate(id: Int, packet: Packet?): Packet {
        return when {
            replacements.contains(id) -> replacements[id]!!.invoke()
            redirects.contains(id) && packet != null -> redirects[id]!!.invoke(packet)
            wrappers.contains(id) && packet != null -> wrappers[id]!!.invoke(packet).also { it.wrapperId = packet.rawId }
            else -> packet!!
        }
    }

    @JvmStatic
    fun isTranslated(id: Int) = replacements.contains(id) || redirects.contains(id) || wrappers.contains(id)

    @JvmStatic
    fun wrap(packet: Packet): Packet? = wrappers[packet.rawId]?.invoke(packet).also { it?.wrapperId = packet.rawId }

    @JvmStatic
    fun redirect(packet: Packet) = redirects[packet.rawId]?.invoke(packet)

    @JvmStatic
    fun handle(packet: Packet, handler: NetworkHandler) = handlers[packet.rawId]?.invoke(packet, handler)

    @JvmStatic
    fun hasWrapper(id: Int) = id in wrappers

    @JvmStatic
    fun hasRedirect(id: Int) = redirects.containsKey(id)

    @JvmStatic
    fun isReplaced(id: Int) = replacements.containsKey(id)

    @JvmStatic
    fun hasApplier(id: Int) = id in handlers

    fun reset() {
        wrappers.clear()
        redirects.clear()
        replacements.clear()
        handlers.clear()
        clientBoundIds = VANILLA_CLIENTBOUND_IDS.toMutableSet()
        serverBoundIds = VANILLA_SERVERBOUND_IDS.toMutableSet()
    }

    @Suppress("unused")
    fun register(id: Int, replacement: () -> Packet) {
        replacements[id] = replacement
    }

    fun <T : Packet> registerWrapper(id: Int, wrapper: (T) -> PacketWrapper<T>) {
        wrappers[id] = wrapper as (Packet) -> PacketWrapper<out Packet>
    }

    fun <T : Packet> registerRedirect(id: Int, redirect: (T) -> Packet) {
        redirects[id] = redirect as (Packet) -> Packet
    }

    fun <T : Packet> registerHandler(id: Int, handler: (T, NetworkHandler) -> Unit) {
        handlers[id] = handler as (Packet, NetworkHandler) -> Unit
    }
}
