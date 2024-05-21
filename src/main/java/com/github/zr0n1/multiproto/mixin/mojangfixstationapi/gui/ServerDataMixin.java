package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(ServerData.class)
public abstract class ServerDataMixin implements MultiprotoServerData {

    @Unique
    private ProtocolVersion multiproto_version;

    @Redirect(method = "load", at = @At(value = "NEW", target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/ServerData;"))
    private static ServerData load(NbtCompound nbt) {
        return MultiprotoServerData.create(nbt);
    }

    @Inject(method = "save()Lnet/minecraft/nbt/NbtCompound;", at = @At(value = "TAIL"))
    private void save(CallbackInfoReturnable<NbtCompound> cir, @Local NbtCompound nbt) {
        nbt.putString("version", multiproto_version.toString());
    }

    @Override
    @Unique
    public ProtocolVersion multiproto_getVersion() {
        return multiproto_version;
    }

    @Override
    @Unique
    public void multiproto_setVersion(ProtocolVersion version) {
        this.multiproto_version = version;
    }
}
