package com.github.zr0n1.multiproto.api.packet;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.ToIntFunction;

public class DataType<T> {

    public static final DataType<Byte> BYTE = new DataType<Byte>(Byte.class, DataInputStream::readByte, DataOutputStream::writeByte, Byte.BYTES);
    public static final DataType<Short> SHORT = new DataType<Short>(Short.class, DataInputStream::readShort, DataOutputStream::writeShort, Short.BYTES);
    public static final DataType<Integer> INT = new DataType<>(Integer.class, DataInputStream::readInt, DataOutputStream::writeInt, Integer.BYTES);
    public static final DataType<Long> LONG = new DataType<>(Long.class, DataInputStream::readLong, DataOutputStream::writeLong, Long.BYTES);
    public static final DataType<Float> FLOAT = new DataType<>(Float.class, DataInputStream::readFloat, DataOutputStream::writeFloat, Float.BYTES);
    public static final DataType<Double> DOUBLE = new DataType<>(Double.class, DataInputStream::readDouble, DataOutputStream::writeDouble, Double.BYTES);
    public static final DataType<Boolean> BOOLEAN = new DataType<>(Boolean.class, DataInputStream::readBoolean, DataOutputStream::writeBoolean, 0);
    public static final DataType<String> UTF = new DataType<>(String.class, DataInput::readUTF, DataOutputStream::writeUTF, String::length);

    public static final DataType<ItemStack> ITEM_STACK = new DataType<>(ItemStack.class, (stream) -> {
        // read
        short id = stream.readShort();
        if (id >= 0) {
            byte count = stream.readByte();
            short damage = VersionManager.isBefore(Version.BETA_8) ? stream.readByte() : stream.readShort();
            return new ItemStack(id, count, damage);
        }
        return null;
    }, (stream, stack) -> {
        // write
        if (stack == null) {
            stream.writeShort(-1);
        } else {
            stream.writeShort(stack.itemId);
            stream.writeByte(stack.count);
            if (VersionManager.isBefore(Version.BETA_8)) stream.writeByte(stack.getDamage());
            else stream.writeShort(stack.getDamage());
        }
    }, stack -> (VersionManager.isBefore(Version.BETA_8) ? 6 : 7));

    public final Class<T> clazz;
    private final Reader<T> reader;
    private final Writer<T> writer;
    private final ToIntFunction<T> sizeFunc;

    public DataType(Class<T> clazz, Reader<T> reader, Writer<T> writer, int size) {
        this.clazz = clazz;
        this.reader = reader;
        this.writer = writer;
        this.sizeFunc = t -> size;
    }

    public DataType(Class<T> clazz, Reader<T> reader, Writer<T> writer, ToIntFunction<T> sizeFunc) {
        this.clazz = clazz;
        this.reader = reader;
        this.writer = writer;
        this.sizeFunc = sizeFunc;
    }

    public static DataType<String> string(int maxLength, int size) {
        return new DataType<>(String.class, stream -> Packet.readString(stream, maxLength), (stream, s) -> Packet.writeString(s, stream), size);
    }

    public static DataType<String> string(int maxLength) {
        return new DataType<>(String.class, stream -> Packet.readString(stream, maxLength), (stream, s) -> Packet.writeString(s, stream), String::length);
    }

    @SuppressWarnings("unchecked")
    public static <T> DataType<T> dummy(T t) {
        return new DataType<>((Class<T>) t.getClass(), stream -> t, (stream, object) -> {
        }, 0);
    }

    public static DataType<?> of(Class<?> clazz) {
        if (clazz == Integer.class || clazz == int.class) return INT;
        if (clazz == Byte.class || clazz == byte.class) return BYTE;
        if (clazz == Short.class || clazz == short.class) return SHORT;
        if (clazz == Long.class || clazz == long.class) return LONG;
        if (clazz == Boolean.class || clazz == boolean.class) return BOOLEAN;
        if (clazz == String.class) return VersionManager.isBefore(Version.BETA_11) ? UTF : string(32767);
        if (clazz == ItemStack.class) return ITEM_STACK;
        return null;
    }

    public T read(DataInputStream stream) throws IOException {
        return this.reader.read(stream);
    }

    public void write(DataOutputStream stream, Object v) throws IOException {
        try {
            this.writer.write(stream, cast(v));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("dont do that");
        }
    }

    public int size(Object obj) {
        try {
            this.sizeFunc.applyAsInt(cast(obj));
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("dont do that");
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private T cast(Object obj) throws ClassCastException {
        if (obj == null) return null;
        if (obj instanceof Integer i && clazz == Byte.class) return (T) Byte.valueOf(i.byteValue());
        if (obj instanceof Integer i && clazz == Short.class) return (T) Short.valueOf(i.shortValue());
        if (obj instanceof Integer i && clazz == Long.class) return (T) Long.valueOf(i.longValue());
        return clazz.cast(obj);
    }

    @FunctionalInterface
    public interface Reader<T> {
        T read(DataInputStream stream) throws IOException;
    }

    @FunctionalInterface
    public interface Writer<T> {
        void write(DataOutputStream stream, T v) throws IOException;
    }
}
