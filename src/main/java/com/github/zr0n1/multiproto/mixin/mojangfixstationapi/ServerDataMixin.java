package com.github.zr0n1.multiproto.mixin.mojangfixstationapi;

import com.github.zr0n1.multiproto.interfaces.MultiprotoServerData;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.telvarost.mojangfixstationapi.client.gui.multiplayer.ServerData;

@Mixin(ServerData.class)
public abstract class ServerDataMixin implements MultiprotoServerData {
    @Unique
    private ProtocolVersion version;

    @Inject(method = "save()Lnet/minecraft/util/io/CompoundTag;",
    at = @At(value = "RETURN", target = "Lpl/telvarost/mojangfixstationapi/client/gui/multiplayer/ServerData;save()Lnet/minecraft/nbt/NbtCompound;"))
    private void save(CallbackInfoReturnable<CompoundTag> cir, @Local CompoundTag nbt) {
        nbt.put("version", version.toString());
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
