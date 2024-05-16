package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(ServerData.class)
public abstract class ServerDataMixin implements MultiprotoServerData {

    @Unique
    private ProtocolVersion version;

    @ModifyArgs(method = "load", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z"))
    private static void load(Args args, NbtList nbt, @Local int i) {
        args.set(0, MultiprotoServerData.constructor((NbtCompound)nbt.get(i)));
    }

    @Inject(method = "save()Lnet/minecraft/nbt/NbtCompound;",
    at = @At(value = "RETURN", target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/ServerData;save()Lnet/minecraft/nbt/NbtCompound;"))
    private void save(CallbackInfoReturnable<NbtCompound> cir, @Local NbtCompound nbt) {
        nbt.putString("version", version.toString());
    }

    @Override
    public ProtocolVersion getVersion() {
        return version;
    }

    @Override
    public void setVersion(ProtocolVersion version) {
        this.version = version;
    }
}
