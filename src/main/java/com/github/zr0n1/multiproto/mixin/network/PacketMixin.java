package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.protocol.Version;
import com.github.zr0n1.multiproto.protocol.packet.PacketWrapper;
import com.github.zr0n1.multiproto.protocol.Protocol;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(value = Packet.class)
@SuppressWarnings("deprecation")
public abstract class PacketMixin {

    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static void multiproto_wrapPacket(int id, CallbackInfoReturnable<Packet> cir) {
        if (Protocol.hasWrapper(id)) cir.setReturnValue(Protocol.wrap(cir.getReturnValue()));
    }

    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private static void multiproto_replacePacket(int id, CallbackInfoReturnable<Packet> cir) {
        if (Protocol.isReplaced(id)) cir.setReturnValue(Protocol.replace(id));
    }

    @Inject(method = "getRawId", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("all")
    private void multiproto_getWrapperId(CallbackInfoReturnable<Integer> cir) {
        if ((Packet) (Object) this instanceof PacketWrapper<?> wrapper) cir.setReturnValue(wrapper.wrapperId);
    }

    @Inject(method = "readString", at = @At("HEAD"), cancellable = true)
    private static void multiproto_readString(DataInputStream stream, int maxLength, CallbackInfoReturnable<String> cir)
            throws IOException {
        if (Protocol.getVer().isLE(Version.B1_4_01)) cir.setReturnValue(stream.readUTF());
    }

    @Inject(method = "writeString", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeShort(I)V"),
            cancellable = true)
    private static void multiproto_writeString(String string, DataOutputStream stream, CallbackInfo ci)
            throws IOException {
        if (Protocol.getVer().isLE(Version.B1_4_01)) {
            stream.writeUTF(string);
            ci.cancel();
        }
    }
}
