package com.github.zr0n1.multiproto.mixin.network.packet;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(Packet.class)
public class PacketMixin {

    @Inject(method = "readString", at = @At("HEAD"), cancellable = true)
    private static void readUTFIfOldVersion(DataInputStream stream, int maxLength, CallbackInfoReturnable<String> cir)
            throws IOException {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_11)) {
            String s = stream.readUTF();
            if (s.length() > maxLength) {
                throw new IOException("Received string length longer than maximum allowed (" + s.length() + " > " + maxLength + ")");
            }
            cir.setReturnValue(s);
        }
    }

    @Inject(method = "writeString", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V"), cancellable = true)
    private static void writeUTFIfOldVersion(String string, DataOutputStream stream, CallbackInfo ci) throws IOException {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_11)) {
            stream.writeUTF(string);
            ci.cancel();
        }
    }
}
