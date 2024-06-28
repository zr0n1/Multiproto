package com.github.zr0n1.multiproto.protocol.packet

import com.github.zr0n1.multiproto.protocol.Protocol
import com.github.zr0n1.multiproto.protocol.Version
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.Packet
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Suppress("unchecked_cast")
interface DataType<T> {
    fun read(stream: DataInputStream): T
    fun write(stream: DataOutputStream, t: Any?)
    fun size(t: Any?): Int

    class Raw<T : Any> (
        private val type: KClass<T>,
        private val read: (DataInputStream) -> T,
        private val write: (DataOutputStream, T) -> Unit,
        private val size: (T) -> Int
    ) : DataType<T> {
        constructor(type: KClass<T>, read: (DataInputStream) -> T, write: (DataOutputStream, T) -> Unit, size: Int) :
                this(type, read, write, { size })

        override fun read(stream: DataInputStream): T = read.invoke(stream)
        override fun write(stream: DataOutputStream, t: Any?) { cast(t)?.let { write.invoke(stream, it) } }
        override fun size(t: Any?) = cast(t)?.let { size.invoke(it) } ?: 0

        private fun cast(obj: Any?): T? {
            return if (obj is Number && type.isSubclassOf(Number::class)) {
                when(type) {
                    Byte::class -> obj.toByte() as T
                    Short::class -> obj.toShort() as T
                    Int::class -> obj.toInt() as T
                    Long::class -> obj.toLong() as T
                    Float::class-> obj.toFloat() as T
                    Double::class -> obj.toDouble() as T
                    else -> obj as? T
                }
            } else obj as? T
        }
    }

    companion object {
        val BYTE: DataType<Byte> = Raw(
            Byte::class,
            DataInputStream::readByte,
            { stream: DataOutputStream, v: Byte -> stream.writeByte(v.toInt()) },
            Byte.SIZE_BYTES
        )

        val SHORT: DataType<Short> = Raw(
            Short::class,
            DataInputStream::readShort,
            { stream: DataOutputStream, v: Short -> stream.writeShort(v.toInt()) },
            Short.SIZE_BYTES
        )

        val INT: DataType<Int> = Raw(
            Int::class,
            DataInputStream::readInt,
            DataOutputStream::writeInt,
            Int.SIZE_BYTES
        )

        val LONG: DataType<Long> = Raw(
            Long::class,
            DataInputStream::readLong,
            DataOutputStream::writeLong,
            Long.SIZE_BYTES
        )

        val FLOAT: DataType<Float> = Raw(
            Float::class,
            DataInputStream::readFloat,
            DataOutputStream::writeFloat,
            Float.SIZE_BYTES
        )

        val DOUBLE: DataType<Double> = Raw(
            Double::class,
            DataInputStream::readDouble,
            DataOutputStream::writeDouble,
            java.lang.Double.BYTES
        )

        val BOOLEAN: DataType<Boolean> = Raw(
            Boolean::class,
            DataInputStream::readBoolean,
            DataOutputStream::writeBoolean,
            0
        )

        val UTF: DataType<String> = Raw(
            String::class,
            DataInputStream::readUTF,
            DataOutputStream::writeUTF
        ) { it.length }

        val ITEM_STACK : DataType<ItemStack?> = object : DataType<ItemStack?> {
            override fun read(stream: DataInputStream): ItemStack? {
                val id = stream.readShort()
                if (id >= 0) {
                    val count = stream.readByte()
                    val damage = if (Protocol.version <= Version.B1_1_02) stream.readByte().toShort()
                    else stream.readShort()
                    return ItemStack(id.toInt(), count.toInt(), damage.toInt())
                } else return null
            }

            override fun write(stream: DataOutputStream, t: Any?) {
                if (t == null) {
                    stream.writeShort(-1)
                } else if (t is ItemStack) {
                    stream.writeShort(t.itemId)
                    stream.writeByte(t.count)
                    if (Protocol.version <= Version.B1_1_02) stream.writeByte(t.damage) else stream.writeShort(t.damage)
                }
            }

            override fun size(t: Any?): Int = if (Protocol.version <= Version.B1_1_02) 4 else 5
        }

        fun string(maxLength: Int, size: Int) = Raw(
            String::class,
            { Packet.readString(it, maxLength) },
            { stream, s -> Packet.writeString(s, stream) },
            size
        )

        fun string(maxLength: Int) = Raw(
            String::class,
            { Packet.readString(it, maxLength) },
            { stream, s -> Packet.writeString(s, stream) }
        ) { it.length }

        fun <T : Any> dummy(t: T): DataType<T> = object : DataType<T> {
            override fun read(stream: DataInputStream) = t
            override fun write(stream: DataOutputStream, t: Any?) { }
            override fun size(t: Any?): Int = 0
        }

        private val TYPES = mapOf(
            Byte::class.java to BYTE,
            Short::class.java to SHORT,
            Int::class.java to INT,
            Long::class.java to LONG,
            Float::class.java to FLOAT,
            Boolean::class.java to BOOLEAN,
            Double::class.java to DOUBLE,
            String::class.java to string(Int.MAX_VALUE),
            ItemStack::class.java to ITEM_STACK
        )

        inline fun <reified T : Any> array(type: Raw<T>): DataType<Array<T>> {
            return Raw(
                Array<T>::class,
                { stream: DataInputStream -> Array(stream.readShort().toInt()) { type.read(stream) } },
                { stream: DataOutputStream, v: Array<T> ->
                    stream.writeShort(v.size)
                    v.forEach { type.write(stream, it) }
                }, {
                    2 + run {
                        var size = 0
                        it.forEach { entry -> size += type.size(entry) }
                        size
                    }
                }
            )
        }

        fun <T : Any> of(type: Class<T>): DataType<T>? {
            return if (type.isArray) {
                array(of(type.componentType) as Raw<Any>) as? Raw<T>
            } else TYPES[type] as? DataType<T>
        }
    }
}