package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(Packet.class)
public class PacketMixin {

    @Redirect(method = "read(Ljava/io/DataInputStream;Z)Lnet/minecraft/network/packet/Packet;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;size()I"))
    private static int translateSize(Packet packet) {
        return PacketTranslator.size(packet);
    }

    @Redirect(method = "read(Ljava/io/DataInputStream;Z)Lnet/minecraft/network/packet/Packet;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;read(Ljava/io/DataInputStream;)V"))
    private static void translateRead(Packet packet, DataInputStream stream) throws IOException {
        PacketTranslator.read(packet, stream);
    }

    @Redirect(method = "write(Lnet/minecraft/network/packet/Packet;Ljava/io/DataOutputStream;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;write(Ljava/io/DataOutputStream;)V"))
    private static void translateWrite(Packet packet, DataOutputStream stream) throws IOException {
        PacketTranslator.write(packet, stream);
    }

    @Inject(method = "readString", at = @At("HEAD"), cancellable = true)
    private static void readUTFBeforeBeta_11(DataInputStream stream, int maxLength, CallbackInfoReturnable<String> cir)
            throws IOException {
        if (VersionManager.isBefore(Version.BETA_11)) cir.setReturnValue(stream.readUTF());
    }

    @Inject(method = "writeString", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V"), cancellable = true)
    private static void writeUTFBeforeBeta_11(String string, DataOutputStream stream, CallbackInfo ci) throws IOException {
        if (VersionManager.isBefore(Version.BETA_11)) {
            stream.writeUTF(string);
            ci.cancel();
        }
    }
}