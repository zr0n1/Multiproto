package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.api.packet.PacketWrapper;
import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.VersionManager;
import com.github.zr0n1.multiproto.protocol.packet.PacketTranslator;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(value = Packet.class, priority = 1001)
public abstract class PacketMixin {

    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static void wrapPacket(int id, CallbackInfoReturnable<Packet> cir) {
        if (PacketTranslator.isWrapped(id)) cir.setReturnValue(PacketTranslator.wrap(cir.getReturnValue()));
    }

    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private static void replacePacket(int id, CallbackInfoReturnable<Packet> cir) {
        if (PacketTranslator.isReplaced(id)) cir.setReturnValue(PacketTranslator.replace(id));
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "getRawId", at = @At("HEAD"), cancellable = true)
    private void getWrapperId(CallbackInfoReturnable<Integer> cir) {
        Packet packet = (Packet) (Object) this;
        if (PacketTranslator.isWrapped(packet)) cir.setReturnValue(((PacketWrapper<Packet>) packet).id);
    }

    @Inject(method = "getRawId", at = @At("HEAD"), cancellable = true)
    private void getReplacementId(CallbackInfoReturnable<Integer> cir) {
        Packet packet = (Packet) (Object) this;
        if (PacketTranslator.isReplaced(packet)) cir.setReturnValue(PacketTranslator.getReplacementId(packet));
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
