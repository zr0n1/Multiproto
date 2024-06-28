package com.github.zr0n1.multiproto.api.packet

class FieldEntry<T>(
    val handler: DataType<T>,
    var fieldIndex: Int = -1,
    val writeValue: (Any) -> T? = { null },
    val unique: Boolean = false
) {
    companion object {
        @JvmStatic
        fun <T : Any> dummy(t: T, fieldIndex: Int = -1): FieldEntry<T> {
            return FieldEntry(DataType.dummy(t), fieldIndex)
        }

        @JvmStatic
        fun <T> unique(handler: DataType<T>, writeValue: T): FieldEntry<T> {
            return FieldEntry(handler, -1, { writeValue }, unique = true)
        }

        @JvmStatic
        @Suppress("unused")
        fun <T> unique(handler: DataType<T>, writeValue: (Any) -> T): FieldEntry<T> {
            return FieldEntry(handler, -1, writeValue, unique = true)
        }
    }
}
