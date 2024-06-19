package com.github.zr0n1.multiproto.api.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketWrapper<T> extends Packet {

    public final int id;
    private final List<FieldEntry<?>> ENTRIES = new ArrayList<>();
    public T holder;
    public Field[] fields;
    protected BiConsumer<T, NetworkHandler> applyFunc;
    protected Consumer<T> postWriteFunc = t -> {
    };

    public PacketWrapper(int id, FieldEntry<?>... entries) {
        this(id, null, entries);
    }

    public PacketWrapper(int id, T t, FieldEntry<?>... entries) {
        this.id = id;
        if (t != null) wrap(t);
        int i = 0;
        for (FieldEntry<?> entry : entries = Arrays.stream(entries).filter(Objects::nonNull).toArray(FieldEntry[]::new)) {
            if (!entry.unique && entry.fieldIndex == -1) {
                entry.fieldIndex = i;
                i++;
            }
        }
        this.ENTRIES.addAll(List.of(entries));
    }

    public PacketWrapper<T> wrap(T t) {
        this.holder = t;
        this.fields = Arrays.stream(holder.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())
                        && !Modifier.isFinal(field.getModifiers()))
                .toArray(Field[]::new);
        if (holder instanceof Packet && applyFunc == null)
            this.applyFunc = (packet, handler) -> ((Packet) packet).apply(handler);
        return this;
    }

    public PacketWrapper<T> infer() {
        if (this.ENTRIES.isEmpty()) {
            for (Field field : fields) {
                DataType<?> type = DataType.of(field);
                if (type != null) ENTRIES.add(FieldEntry.of(type));
            }
        }
        return this;
    }

    @Override
    public void read(DataInputStream stream) {
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                Object obj = entry.onRead(entry.type.read(stream));
                if (!entry.unique) {
                    fields[entry.fieldIndex].set(holder, obj);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                Object v = entry.value(holder) != null ? entry.value(holder) : fields[entry.fieldIndex].get(holder);
                if (!entry.unique) {
                    fields[entry.fieldIndex].set(holder, v);
                }
                entry.type.write(stream, entry.onWrite(v));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        postWriteFunc.accept(holder);
    }

    @Override
    public int size() {
        int size = 0;
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                size += entry.type.size(entry.value(holder) != null ? entry.value(holder) : fields[entry.fieldIndex].get(holder));
            } catch (IllegalAccessException ignored) {
            }
        }
        return size;
    }

    @Override
    public void apply(NetworkHandler handler) {
        applyFunc.accept(holder, handler);
    }

    public PacketWrapper<T> apply(BiConsumer<T, NetworkHandler> applyFunc) {
        this.applyFunc = applyFunc;
        return this;
    }

    public PacketWrapper<T> postWrite(Consumer<T> func) {
        this.postWriteFunc = func;
        return this;
    }
}
