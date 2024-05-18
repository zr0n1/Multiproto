package com.github.zr0n1.multiproto.mixin.network.packet.login;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;

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

    @Shadow public String username;
    @Shadow public int protocolVersion;
    @Shadow public long worldSeed;
    @Shadow public byte dimensionId;
    @Unique public String password = "Password";

    @Redirect(method = "write", at = @At(value = "FIELD", target = "Lnet/minecraft/network/packet/login/LoginHelloPacket;protocolVersion:I"))
    private int redirectProtocolVersion(LoginHelloPacket instance) {
        return Multiproto.getVersion().version;
    }

    @Inject(method = "read", at = @At("HEAD"), cancellable = true)
    private void read(DataInputStream stream, CallbackInfo ci) throws IOException {
        ProtocolVersion v = Multiproto.getVersion();
        protocolVersion = stream.readInt();
        username = readString(stream, 16);
        if(v.compareTo(ProtocolVersion.BETA_11) < 0) password = stream.readUTF();
        if(v.compareTo(ProtocolVersion.ALPHA_LATER_3) >= 0) {
            worldSeed = stream.readLong();
            dimensionId = stream.readByte();
        }
        ci.cancel();
    }

    @Inject(method = "write", at = @At("HEAD"), cancellable = true)
    private void write(DataOutputStream stream, CallbackInfo ci) throws IOException {
        ProtocolVersion v = Multiproto.getVersion();
        stream.writeInt(protocolVersion = v.version);
        writeString(username, stream);
        if(v.compareTo(ProtocolVersion.BETA_11) < 0) writeString(password, stream);
        if(v.compareTo(ProtocolVersion.ALPHA_LATER_3) >= 0) {
            stream.writeLong(worldSeed);
            stream.writeByte(dimensionId);
        }
        Multiproto.LOGGER.info("Logging in as {} with protocol version {}", username, protocolVersion);
        ci.cancel();
    }

    @Inject(method = "size", at = @At("HEAD"), cancellable = true)
    private void size(CallbackInfoReturnable<Integer> cir) {
        ProtocolVersion v = Multiproto.getVersion();
        if(v.compareTo(ProtocolVersion.BETA_11) < 0) {
            cir.setReturnValue(4 + username.length() + password.length() + 4 + (v.compareTo(ProtocolVersion.ALPHA_LATER_3) >= 0 ? 5 : 0));
        }
    }
}
