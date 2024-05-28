package com.github.zr0n1.multiproto.api.packet;

import com.github.zr0n1.multiproto.Multiproto;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketWrapper<T> extends Packet {

    public final int id;
    public final List<FieldEntry<?>> ENTRIES = new ArrayList<>();
    public T holder;
    public Field[] fields;
    protected BiConsumer<T, NetworkHandler> applyFunc = (t, handler) -> {};
    protected Consumer<T> postWriteFunc = t -> {};

    public PacketWrapper(int id, FieldEntry<?>... entries) {
        this.id = id;
        this.ENTRIES.addAll(Arrays.asList(entries));
    }

    public PacketWrapper(int id, T t, FieldEntry<?>... entries) {
        this.id = id;
        this.holder = t;
        this.ENTRIES.addAll(Arrays.asList(entries));
    }

    public PacketWrapper<T> wrap(T t) {
        this.holder = t;
        this.fields = Arrays.stream(holder.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())
                        && !Modifier.isFinal(field.getModifiers()))
                .toArray(Field[]::new);
        if(holder instanceof Packet) this.applyFunc = (packet, handler) -> ((Packet) packet).apply(handler);
        return this;
    }

    @Override
    public void read(DataInputStream stream) {
        int i = 0;
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                Object obj = entry.onRead(entry.type.read(stream));
                if (!entry.unique) {
                    int index = entry.fieldIndex > -1 ? entry.fieldIndex : i;
                    fields[index].set(holder, obj);
                    if (entry.fieldIndex == -1) i++;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(DataOutputStream stream){
        int i = 0;
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                int index = entry.fieldIndex > -1 ? entry.fieldIndex : i;
                Object v = entry.value(holder) != null ? entry.value(holder) : fields[index].get(holder);
                if (!entry.unique) {
                    fields[index].set(holder, v);
                    if (entry.fieldIndex == -1) i++;
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
        int i = 0;
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                int index = entry.fieldIndex > -1 ? entry.fieldIndex : i;
                size += entry.type.size(entry.value(holder) != null ? entry.value(holder) : fields[index].get(holder));
                if (entry.fieldIndex == -1 && !entry.unique) i++;
            } catch (IllegalAccessException ignored) {
            }
        }
        return size;
    }

    @SuppressWarnings("unchecked")
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
