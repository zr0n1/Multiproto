package com.github.zr0n1.multiproto.api.packet;

import net.minecraft.network.packet.Packet;

import java.util.function.Consumer;
import java.util.function.Function;

public class FieldEntry<T> {

    public final DataType<T> type;
    public final boolean unique;
    public final int declarationOrder;
    private final Consumer<T> onRead;
    private final Function<Packet, T> uniqueValue;

    private FieldEntry(DataType<T> type, boolean unique, int declarationOrder, Consumer<T> onRead, Function<Packet, T> uniqueValue) {
        this.type = type;
        this.unique = unique;
        this.declarationOrder = declarationOrder;
        this.onRead = onRead;
        this.uniqueValue = uniqueValue;
    }

    public static <T> FieldEntry<T> of(DataType<T> type) {
        return of(type, -1);
    }

    public static <T> FieldEntry<T> of(DataType<T> type, int declarationOrder) {
        return new FieldEntry<>(type, false, declarationOrder, null, null);
    }

    public static <T> FieldEntry<T> unique(DataType<T> type, T value) {
        return unique(type, t -> {
        }, packet -> value);
    }

    public static <T, P extends Packet> FieldEntry<T> unique(DataType<T> type, Function<P, T> uniqueValue) {
        return unique(type, t -> {
        }, uniqueValue);
    }

    public static <T, P extends Packet> FieldEntry<T> unique(DataType<T> type, Consumer<T> onRead, Function<P, T> uniqueValue) {
        return new FieldEntry<>(type, true, -1, onRead, (Function<Packet, T>) uniqueValue);
    }

    public void onRead(Object object) {
        onRead.accept((T) object);
    }

    public Object uniqueValue(Packet packet) {
        return uniqueValue.apply(packet);
    }
}
