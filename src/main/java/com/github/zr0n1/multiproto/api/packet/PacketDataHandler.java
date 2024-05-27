package com.github.zr0n1.multiproto.api.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class PacketDataHandler<P extends Packet> {

    private final FieldEntry<?>[] ENTRIES;
    private BiConsumer<P, NetworkHandler> applyFunction = Packet::apply;

    public PacketDataHandler(FieldEntry<?>... entries) {
        this.ENTRIES = entries;
    }

    public PacketDataHandler(FieldEntry<?>[] entries, BiConsumer<P, NetworkHandler> applyFunction) {
        this.ENTRIES = entries;
        this.applyFunction = applyFunction;
    }

    public void read(Packet packet, DataInputStream stream) throws IOException {
        Field[] fields = Arrays.stream(packet.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .toArray(Field[]::new);
        for (int i = 0; i < ENTRIES.length; i++) {
            FieldEntry<?> entry = ENTRIES[i];
            try {
                if (!entry.unique)
                    fields[entry.declarationOrder > -1 ? entry.declarationOrder : i].set(packet, entry.type.read(stream));
                else entry.onRead(entry.type.read(stream));
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public void write(Packet packet, DataOutputStream stream) throws IOException {
        Field[] fields = Arrays.stream(packet.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .toArray(Field[]::new);
        for (int i = 0; i < ENTRIES.length; i++) {
            FieldEntry<?> entry = ENTRIES[i];
            try {
                if (!entry.unique)
                    entry.type.write(stream, fields[entry.declarationOrder > -1 ? entry.declarationOrder : i].get(packet));
                else entry.type.write(stream, entry.uniqueValue(packet));
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public int size(Packet packet) {
        Field[] fields = Arrays.stream(packet.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
                .toArray(Field[]::new);
        int size = 0;
        for (int i = 0; i < ENTRIES.length; i++) {
            FieldEntry<?> entry = ENTRIES[i];
            try {
                if (!entry.unique)
                    size += entry.type.size(fields[entry.declarationOrder > -1 ? entry.declarationOrder : i].get(packet));
                else size += entry.type.size(entry.uniqueValue(packet));
            } catch (IllegalAccessException ignored) {
            }
        }
        return size;
    }

    public void apply(Packet packet, NetworkHandler handler) {
        applyFunction.accept((P) packet, handler);
    }
}
