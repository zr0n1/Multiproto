package com.github.zr0n1.multiproto.protocol.packet;

import com.github.zr0n1.multiproto.api.packet.PacketWrapper;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketTranslator {

    private static final Map<Integer, Function<Packet, PacketWrapper<Packet>>> WRAPPERS = new HashMap<>();
    private static final Map<Integer, Function<Packet, Packet>> C2S_REDIRECTS = new HashMap<>();
    private static final BiMap<Integer, Packet> REPLACEMENTS = HashBiMap.create();
    private static final Map<Integer, BiConsumer<Packet, NetworkHandler>> APPLIERS = new HashMap<>();

    public static Packet wrap(Packet packet) {
        return packet != null ? WRAPPERS.get(packet.getRawId()).apply(packet) : null;
    }

    public static Packet redirect(Packet packet) {
        return C2S_REDIRECTS.containsKey(packet.getRawId()) ? C2S_REDIRECTS.get(packet.getRawId()).apply(packet) : packet;
    }

    public static Packet replace(int id) {
        return REPLACEMENTS.get(id);
    }

    public static void apply(Packet packet, NetworkHandler handler) {
        APPLIERS.get(packet.getRawId()).accept(packet, handler);
    }

    public static boolean isWrapped(int id) {
        return WRAPPERS.containsKey(id);
    }

    public static boolean isWrapped(Packet packet) {
        return packet instanceof PacketWrapper<?> wrapper && wrapper.holder instanceof Packet;
    }

    public static boolean isRedirected(int id) {
        return C2S_REDIRECTS.containsKey(id);
    }

    public static boolean isReplaced(int id) {
        return REPLACEMENTS.containsKey(id);
    }

    public static boolean isReplaced(Packet packet) {
        return REPLACEMENTS.containsValue(packet);
    }

    public static boolean hasApplier(int id) {
        return APPLIERS.containsKey(id);
    }

    public static int getReplacementId(Packet packet) {
        return REPLACEMENTS.inverse().get(packet);
    }

    public static void applyChanges() {
        WRAPPERS.clear();
        C2S_REDIRECTS.clear();
        REPLACEMENTS.clear();
        APPLIERS.clear();
        BetaPackets.applyChanges();
        BetalphaPackets.applyChanges();
        AlphaPackets.applyChanges();
    }

    @SuppressWarnings("unchecked")
    public static void wrapLE(Version target, PacketWrapper<? extends Packet> wrapper) {
        if (VersionManager.isLE(target))
            WRAPPERS.put(wrapper.id, packet -> ((PacketWrapper<Packet>) wrapper).wrap(packet));
    }

    @SuppressWarnings("unchecked")
    public static void wrapFrom(Version max, Version min, PacketWrapper<? extends Packet> wrapper) {
        if (VersionManager.isWithin(max, min))
            WRAPPERS.put(wrapper.id, packet -> ((PacketWrapper<Packet>) wrapper).wrap(packet));
    }

    @SuppressWarnings("unchecked")
    public static void redirectLE(Version target, int id, Function<? extends Packet, Packet> redirect) {
        if (VersionManager.isLE(target)) C2S_REDIRECTS.put(id, (Function<Packet, Packet>) redirect);
    }

    public static void replaceLE(Version target, PacketWrapper<?> packet) {
        replaceLE(target, packet.id, packet);
    }

    public static void replaceLE(Version target, int id, Packet packet) {
        if (VersionManager.isLE(target)) REPLACEMENTS.put(id, packet);
    }

    @SuppressWarnings("unchecked")
    public static void applyLE(Version target, int id, BiConsumer<? extends Packet, NetworkHandler> applier) {
        if (VersionManager.isLE(target)) APPLIERS.put(id, (BiConsumer<Packet, NetworkHandler>) applier);
    }
}
