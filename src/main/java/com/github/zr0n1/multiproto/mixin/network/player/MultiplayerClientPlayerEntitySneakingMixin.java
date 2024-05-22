package com.github.zr0n1.multiproto.mixin.network.player;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.client.util.Session;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.play.EntityAnimationPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MultiplayerClientPlayerEntity.class)
public abstract class MultiplayerClientPlayerEntitySneakingMixin extends ClientPlayerEntity {

    public MultiplayerClientPlayerEntitySneakingMixin(Minecraft minecraft, World world, Session session, int dimensionId) {
        super(minecraft, world, session, dimensionId);
    }

    @Redirect(method = "method_1922", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V",
            ordinal = 0))
    private void redirectSendStartSneakingPacket(ClientNetworkHandler handler, Packet packet) {
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0) handler.sendPacket(packet);
        else handler.sendPacket(new EntityAnimationPacket(this, 104));
    }

    @Redirect(method = "method_1922", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V",
            ordinal = 1))
    private void redirectSendStopSneakingPacket(ClientNetworkHandler handler, Packet packet) {
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) >= 0) handler.sendPacket(packet);
        else handler.sendPacket(new EntityAnimationPacket(this, 105));
    }
}