package com.github.zr0n1.multiproto.api.packet;

import java.util.function.Consumer;
import java.util.function.Function;

public class FieldEntry<T> {

    public final DataType<T> type;
    public final boolean unique;
    public int fieldIndex;
    private final Function<Object, T> valueFunc;
    private Consumer<T> onReadFunc = t -> {
    };
    private Consumer<T> onWriteFunc = t -> {
    };

    private FieldEntry(DataType<T> type, boolean unique, int fieldIndex, Function<Object, T> valueFunc) {
        this.type = type;
        this.unique = unique;
        this.fieldIndex = fieldIndex;
        this.valueFunc = valueFunc;
    }

    public static <T> FieldEntry<T> dummy(T t) {
        return of(DataType.dummy(t));
    }

    public static <T> FieldEntry<T> dummy(T t, int fieldIndex) {
        return of(DataType.dummy(t), fieldIndex);
    }

    public static <T> FieldEntry<T> of(DataType<T> type) {
        return of(type, -1);
    }

    public static <T> FieldEntry<T> of(DataType<T> type, int fieldIndex) {
        return of(type, fieldIndex, (T) null);
    }

    public static <T> FieldEntry<T> of(DataType<T> type, Function<Object, T> valueFunc) {
        return of(type, -1, valueFunc);
    }

    public static <T> FieldEntry<T> of(DataType<T> type, int fieldIndex, T value) {
        return new FieldEntry<>(type, false, fieldIndex, p -> value);
    }

    public static <T> FieldEntry<T> of(DataType<T> type, int fieldIndex, Function<Object, T> valueFunc) {
        return new FieldEntry<>(type, false, fieldIndex, valueFunc);
    }

    public static <T> FieldEntry<T> unique(DataType<T> type, T value) {
        return new FieldEntry<>(type, true, -1, p -> value);
    }

    public static <T> FieldEntry<T> unique(DataType<T> type, Function<Object, T> valueFunc) {
        return new FieldEntry<>(type, true, -1, valueFunc);
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

    public T value(Object obj) {
        return valueFunc.apply(obj);
    }
}
