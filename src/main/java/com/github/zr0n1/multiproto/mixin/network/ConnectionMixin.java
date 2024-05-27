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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Connection.class, priority = 1001)
public abstract class ConnectionMixin {

    @SuppressWarnings("rawtypes")
    @Shadow
    private List field_1286;

    @Shadow
    private NetworkHandler field_1289;
    @Shadow
    private boolean field_1293;
    @Shadow
    private String field_1294;
    @Shadow
    private Object[] field_1295;

    @Shadow
    public abstract void method_1122();

    @Inject(method = "sendPacket", at = @At("HEAD"))
    private void redirectPacket(Packet packet, CallbackInfo ci, @Local(argsOnly = true) LocalRef<Packet> local) {
        local.set(PacketTranslator.redirect(packet));
    }

    @Inject(method = "method_1129", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1),
            cancellable = true)
    private void translateApply(CallbackInfo ci, @Local int var1) {
        while (!this.field_1286.isEmpty() && var1-- >= 0) {
            Packet packet = (Packet) this.field_1286.remove(0);
            // stapi momento
            PacketTranslator.apply(packet, packet instanceof IdentifiablePacket identifiablePacket ?
                    IdentifiablePacketImpl.HANDLERS.getOrDefault(identifiablePacket.getId(), field_1289) : field_1289);
        }

        this.method_1122();
        if (this.field_1293 && this.field_1286.isEmpty()) {
            this.field_1289.onDisconnected(this.field_1294, this.field_1295);
        }
        ci.cancel();
    }

    @Redirect(method = "sendPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int sendPacketWithHandlerSize(Packet packet) {
        return PacketTranslator.size(packet);
    }

    @Redirect(method = "method_1137", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int quinkstanty(Packet packet) {
        return PacketTranslator.size(packet);
    }

    @Redirect(method = "method_1139", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private int ploinkus(Packet packet) {
        return PacketTranslator.size(packet);
    }
}
