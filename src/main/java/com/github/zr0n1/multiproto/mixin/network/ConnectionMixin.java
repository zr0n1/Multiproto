package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.network.Connection;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.impl.network.packet.IdentifiablePacketImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(value = Connection.class, priority = 1001)
public abstract class ConnectionMixin {

    @Shadow
    private NetworkHandler field_1289;

    @Shadow
    @SuppressWarnings("rawtypes")
    private List field_1286;

    @Shadow
    public abstract void method_1122();

    @Shadow
    private boolean field_1293;

    @Shadow
    private String field_1294;

    @Shadow
    private Object[] field_1295;

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void wrapPacket(Packet packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet> packetRef) {
        if (PacketTranslator.isWrapped(packet.getRawId())) packetRef.set(PacketTranslator.wrap(packet));
    }

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void redirectPacket(Packet packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet> packetRef) {
        if (PacketTranslator.isRedirected(packet.getRawId())) packetRef.set(PacketTranslator.redirect(packet));
    }

    // this is so fucking stupid lmao
    @Inject(method = "method_1129", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1), cancellable = true)
    private void applyPackets(CallbackInfo ci, @Local int var1) {
        while (!this.field_1286.isEmpty() && var1-- >= 0) {
            Packet packet = (Packet) this.field_1286.remove(0);
            if (PacketTranslator.hasApplier(packet.getRawId())) {
                PacketTranslator.apply(packet, field_1289);
            } else {
                // stapi momento
                packet.apply(packet instanceof IdentifiablePacket identifiablePacket ?
                        IdentifiablePacketImpl.HANDLERS.getOrDefault(identifiablePacket.getId(), field_1289) : field_1289);
            }
        }
        this.method_1122();
        if (this.field_1293 && this.field_1286.isEmpty()) {
            this.field_1289.onDisconnected(this.field_1294, this.field_1295);
        }
        ci.cancel();
    }
}
