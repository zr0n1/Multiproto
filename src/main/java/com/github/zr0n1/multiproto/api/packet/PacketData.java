package com.github.zr0n1.multiproto.api.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class PacketData<P extends Packet> {

    public final List<FieldEntry<?>> ENTRIES = new ArrayList<>();
    private BiConsumer<P, NetworkHandler> applyFunc = Packet::apply;
    private Consumer<P> postReadFunc = p -> {
    };
    private Consumer<P> postWriteFunc = p -> {
    };

    public PacketData(FieldEntry<?>... entries) {
        this.ENTRIES.addAll(Arrays.asList(entries));
    }

    @SuppressWarnings("unchecked")
    public void read(Packet packet, DataInputStream stream) throws IOException {
        Field[] fields = Arrays.stream(packet.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .toArray(Field[]::new);
        int i = 0;
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                int index = entry.fieldIndex > -1 ? entry.fieldIndex : i;
                Object obj = entry.onRead(entry.type.read(stream));
                if (!entry.unique) {
                    fields[index].set(packet, obj);
                    if (entry.fieldIndex == -1) i++;
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        postReadFunc.accept((P) packet);
    }

    @SuppressWarnings("unchecked")
    public void write(Packet packet, DataOutputStream stream) throws IOException {
        Field[] fields = Arrays.stream(packet.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .toArray(Field[]::new);
        int i = 0;
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                int index = entry.fieldIndex > -1 ? entry.fieldIndex : i;
                Object v = entry.value(packet) != null ? entry.value(packet) : fields[index].get(packet);
                if (!entry.unique) {
                    fields[index].set(packet, v);
                    if (entry.fieldIndex == -1) i++;
                }
                entry.type.write(stream, entry.onWrite(v));
            } catch (IllegalAccessException ignored) {
            }
        }
        postWriteFunc.accept((P) packet);
    }

    public int size(Packet packet) {
        Field[] fields = Arrays.stream(packet.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .toArray(Field[]::new);
        int size = 0;
        int i = 0;
        for (FieldEntry<?> entry : ENTRIES) {
            try {
                int index = entry.fieldIndex > -1 ? entry.fieldIndex : i;
//                Multiproto.LOGGER.info("size, DataType: {}, field: {}, {}",
//                        entry.type.clazz.getName(), index, fields[index].toString());
                size += entry.type.size(entry.value(packet) != null ? entry.value(packet) : fields[index].get(packet));
                if (entry.fieldIndex == -1 && !entry.unique) i++;
            } catch (IllegalAccessException ignored) {
            }
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    public void apply(Packet packet, NetworkHandler handler) {
        applyFunc.accept((P) packet, handler);
    }

    public PacketData<P> apply(BiConsumer<P, NetworkHandler> applyFunc) {
        this.applyFunc = applyFunc;
        return this;
    }

    public PacketData<P> postRead(Consumer<P> func) {
        this.postReadFunc = func;
        return this;
    }

    public PacketData<P> postWrite(Consumer<P> func) {
        this.postWriteFunc = func;
        return this;
    }
}
