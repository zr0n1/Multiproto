package com.github.zr0n1.multiproto.api.packet;

import net.minecraft.network.packet.Packet;

import java.util.function.Consumer;
import java.util.function.Function;

public class FieldEntry<T> {

    public final DataType<T> type;
    public final boolean unique;
    public final int fieldIndex;
    private final Function<Packet, T> valueFunc;
    private Consumer<T> onReadFunc = t -> {
    };
    private Consumer<T> onWriteFunc = t -> {
    };


    private FieldEntry(DataType<T> type, boolean unique, int fieldIndex, Function<Packet, T> valueFunc) {
        this.type = type;
        this.unique = unique;
        this.fieldIndex = fieldIndex;
        this.valueFunc = valueFunc;
    }

    public static <T> FieldEntry<T> of(DataType<T> type) {
        return of(type, -1);
    }

    public static <T> FieldEntry<T> of(DataType<T> type, int fieldIndex) {
        return of(type, fieldIndex, (T) null);
    }

    public static <T, P extends Packet> FieldEntry<T> of(DataType<T> type, Function<P, T> valueFunc) {
        return of(type, -1, valueFunc);
    }

    public static <T> FieldEntry<T> of(DataType<T> type, int fieldIndex, T value) {
        return new FieldEntry<T>(type, false, fieldIndex, p -> value);
    }

    @SuppressWarnings("unchecked")
    public static <T, P extends Packet> FieldEntry<T> of(DataType<T> type, int fieldIndex, Function<P, T> valueFunc) {
        return new FieldEntry<T>(type, false, fieldIndex, (Function<Packet, T>) valueFunc);
    }

    public static <T> FieldEntry<T> unique(DataType<T> type, T value) {
        return new FieldEntry<T>(type, true, -1, p -> value);
    }

    @SuppressWarnings("unchecked")
    public static <T, P extends Packet> FieldEntry<T> unique(DataType<T> type, Function<P, T> valueFunc) {
        return new FieldEntry<T>(type, true, -1, (Function<Packet, T>) valueFunc);
    }

    @SuppressWarnings("unchecked")
    public Object onRead(Object obj) {
        onReadFunc.accept((T) obj);
        return obj;
    }

    @SuppressWarnings("unchecked")
    public Object onWrite(Object obj) {
        onWriteFunc.accept((T) obj);
        return obj;
    }

    public FieldEntry<T> onRead(Consumer<T> func) {
        this.onReadFunc = func;
        return this;
    }

    public FieldEntry<T> onWrite(Consumer<T> func) {
        this.onWriteFunc = func;
        return this;
    }

    public Object value(Packet packet) {
        return valueFunc.apply(packet);
    }
}