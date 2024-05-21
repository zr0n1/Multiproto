package com.github.zr0n1.multiproto.mixin.network.packet.login;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.login.LoginHelloPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(LoginHelloPacket.class)
public abstract class LoginHelloPacketMixin extends Packet {

    @Shadow
    public String username;
    @Shadow
    public int protocolVersion;
    @Unique
    public String password = "Password";

    @Redirect(method = "write", at = @At(value = "FIELD", target = "Lnet/minecraft/network/packet/login/LoginHelloPacket;protocolVersion:I"))
    private int redirectProtocolVersion(LoginHelloPacket instance) {
        return protocolVersion = ProtocolVersionManager.getVersion().version;
    }

    @Inject(method = "read", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/packet/login/LoginHelloPacket;readString(Ljava/io/DataInputStream;I)Ljava/lang/String;",
            shift = At.Shift.AFTER))
    private void injectReadPassword(DataInputStream stream, CallbackInfo ci) throws IOException {
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_11) < 0) password = stream.readUTF();
    }

    @Inject(method = "write", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/packet/login/LoginHelloPacket;writeString(Ljava/lang/String;Ljava/io/DataOutputStream;)V",
            shift = At.Shift.AFTER))
    private void injectWritePassword(DataOutputStream stream, CallbackInfo ci) {
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_11) < 0) writeString(password, stream);
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void injectLog(DataOutputStream stream, CallbackInfo ci) {
        Multiproto.LOGGER.info("Logging in as {} with protocol version {}", username, protocolVersion);
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        ProtocolVersion v = ProtocolVersionManager.getVersion();
        if (v.compareTo(ProtocolVersion.BETA_11) < 0) {
            cir.setReturnValue(4 + username.length() + password.length() + 4 + 5);
            //cir.setReturnValue(4 + username.length() + password.length() + 4 + (v.compareTo(ProtocolVersion.ALPHA_LATER_3) >= 0 ? 5 : 0));
        }
    }
}
