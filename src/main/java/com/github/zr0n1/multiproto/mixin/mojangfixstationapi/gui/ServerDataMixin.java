package com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui;

import com.github.zr0n1.multiproto.mixinterface.MultiprotoServerData;
import com.github.zr0n1.multiproto.protocol.Version;
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
    private Version multiproto$version;

    @Override
    @Unique
    public Version multiproto_getVersion() {
        return multiproto$version;
    }

    @Override
    @Unique
    public void multiproto_setVersion(Version version) {
        this.multiproto$version = version;
    }

    @Redirect(method = "load", at = @At(value = "NEW", target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/ServerData;",
            remap = false), remap = false)
    private static ServerData multiproto_load(NbtCompound nbt) {
        return MultiprotoServerData.create(nbt);
    }

    @Inject(method = "save()Lnet/minecraft/nbt/NbtCompound;", at = @At(value = "TAIL"))
    private void multiproto_save(CallbackInfoReturnable<NbtCompound> cir, @Local NbtCompound nbt) {
        nbt.putString("version", multiproto$version.toString());
    }
}
