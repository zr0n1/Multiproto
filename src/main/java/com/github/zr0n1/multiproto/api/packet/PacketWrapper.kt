package com.github.zr0n1.multiproto.api.packet

import net.minecraft.network.NetworkHandler
import net.minecraft.network.packet.Packet
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.reflect.Modifier

class PacketWrapper<T : Any>(
    private val holder: T,
    vararg entries: FieldEntry<*>?,
    private val apply: (T, NetworkHandler) -> Unit = if (holder is Packet) {
        { packet, handler -> (packet as Packet).apply(handler) }
    } else { _, _ -> },
    private val postWrite: (T) -> Unit = { _: T -> },
    @JvmField
    var wrapperId: Int = -1
) : Packet() {
    private var entries = entries.filterNotNull().toMutableList().apply {
        var i = 0
        forEach {
            if (!it.unique && it.fieldIndex == -1) {
                it.fieldIndex = i
                i++
            }
        }
    }

    private var fields = holder::class.java.declaredFields
        .filter {
            (Modifier.isPublic(it.modifiers) && !Modifier.isStatic(it.modifiers) && !Modifier.isFinal(it.modifiers))
        }
        .toTypedArray()

    private val unique: Array<Any?> = Array(entries.size) {
        if (entries[it]?.unique == true) entries[it]!!.writeValue(holder) else null
    }

    fun infer() = apply {
        if (entries.isEmpty()) {
            fields.forEachIndexed { index, field ->
                DataType.of(field.type)?.let { entries.add(FieldEntry(it, index)) }
            }
        }
    }

    override fun read(stream: DataInputStream) = entries.forEachIndexed { i, it ->
        it.handler.read(stream).also { data ->
            if (it.unique) unique[i] = data
            else fields[it.fieldIndex][holder] = data
        }
    }

    override fun write(stream: DataOutputStream) = entries.forEachIndexed { i, it ->
        it.handler.write(stream,
            unique[i] ?: it.writeValue(holder)?.also { v ->
                fields[it.fieldIndex][holder] = v
            } ?: fields[it.fieldIndex][holder])
    }.also { postWrite(holder) }

    override fun size(): Int {
        var size = 0
        entries.forEachIndexed { i, it ->
            size += (it.handler.size(unique[i] ?: it.writeValue(holder) ?: fields[it.fieldIndex][holder]))
        }
        return size
    }

    override fun apply(handler: NetworkHandler) = apply(holder, handler)
}
