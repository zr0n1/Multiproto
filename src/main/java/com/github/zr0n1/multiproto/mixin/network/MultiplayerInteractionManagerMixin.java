package com.github.zr0n1.multiproto.mixin.network;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.MultiplayerInteractionManager;
import net.minecraft.block.Block;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiplayerInteractionManager.class)
public abstract class MultiplayerInteractionManagerMixin extends InteractionManager {

    @Shadow private ClientNetworkHandler networkHandler;
    @Shadow private boolean field_2615;
    @Shadow private int field_2614;
    @Shadow private float field_2611;
    @Shadow private float field_2612;
    @Shadow private float field_2613;
    @Shadow private int field_2608;
    @Shadow private int field_2609;
    @Shadow private int field_2610;

    public MultiplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Shadow public abstract void method_1707(int i, int j, int k, int l);

    @Inject(method = "clickSlot", at = @At("HEAD"))
    private void modifyShiftClickSlot(CallbackInfoReturnable<ItemStack> cir, @Local(argsOnly = true) LocalBooleanRef shift) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_11) < 0) shift.set(false);
    }

    @Inject(method = "method_1716", at = @At("HEAD"))
    private void sendBlockMined(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) < 0) {
            networkHandler.sendPacket(new PlayerActionC2SPacket(3, i, j, k, l));
        }
    }

    @Inject(method = "method_1707", at = @At(value = "HEAD"), cancellable = true)
    private void mineBlock(int i, int j, int k, int l, CallbackInfo ci) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) < 0) {
            field_2615 = true;
            networkHandler.sendPacket(new PlayerActionC2SPacket(0, i, j, k, l));
            int id = minecraft.world.getBlockId(i, j, k);
            if(id > 0 && field_2611 == 0.0F) {
                Block.BLOCKS[id].onBlockBreakStart(minecraft.world, i, j, k, minecraft.player);
            }
            if (id > 0 && Block.BLOCKS[id].getHardness(this.minecraft.player) >= 1.0F) {
                this.method_1716(i, j, k, l);
            }
            ci.cancel();
        }
    }

    @Inject(method = "method_1705", at = @At("HEAD"), cancellable = true)
    private void resetBlockMining(CallbackInfo ci) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) < 0) {
            if(!field_2615) ci.cancel();
            field_2614 = 0;
            networkHandler.sendPacket(new PlayerActionC2SPacket(2, 0, 0, 0, 0));
        }
    }

    @Inject(method = "method_1721", at = @At("HEAD"))
    private void sendBlockMining(int i, int j, int k, int l, CallbackInfo ci) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) < 0) {
            field_2615 = true;
            networkHandler.sendPacket(new PlayerActionC2SPacket(1, i, j, k, l));
        }
    }

    @Redirect(method = "method_1721", at = @At(value = "FIELD", target = "Lnet/minecraft/MultiplayerInteractionManager;field_2615:Z",
    opcode = Opcodes.PUTFIELD), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I")))
    private void redirectPutField_2615(MultiplayerInteractionManager instance, boolean b) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) >= 0) field_2615 = b;
    }

    @Redirect(method = "method_1721", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/MultiplayerInteractionManager;field_2615:Z",
            opcode = Opcodes.PUTFIELD, ordinal = 1)))
    private void redirectSendBlockMiningPacket(ClientNetworkHandler handler, Packet packet) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) >= 0) handler.sendPacket(packet);
    }

    @Redirect(method = "method_1721", at = @At(value = "INVOKE", target = "Lnet/minecraft/MultiplayerInteractionManager;method_1707(IIII)V"))
    private void redirectMineBlockInSendBlockMining(MultiplayerInteractionManager manager, int i, int j, int k, int l) {
        if(Multiproto.getVersion().compareTo(ProtocolVersion.BETA_9) >= 0) {
            method_1707(i, j, k, l);
        } else {
            field_2611 = 0.0F;
            field_2612 = 0.0F;
            field_2613 = 0.0F;
            field_2608 = i;
            field_2609 = j;
            field_2610 = k;
        }
    }
}
