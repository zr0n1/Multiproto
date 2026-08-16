package com.github.zr0n1.multiproto.mixin.network.player;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.block.Block;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MultiplayerInteractionManager;
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

    @Shadow
    private ClientNetworkHandler networkHandler;
    @Shadow
    private boolean breakingBlock;
    @Shadow
    private int breakingPosX;
    @Shadow
    private int breakingPosY;
    @Shadow
    private int breakingPosZ;
    @Shadow
    private float blockBreakingProgress;
    @Shadow
    private float lastBlockBreakingProgress;
    @Shadow
    private float breakingSoundDelayTicks;
    @Shadow
    private int breakingDelayTicks;

    public MultiplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Shadow
    public abstract void attackBlock(int i, int j, int k, int l);

    @Inject(method = "clickSlot", at = @At("HEAD"))
    private void disableShiftClick(CallbackInfoReturnable<ItemStack> cir, @Local(argsOnly = true) LocalBooleanRef shift) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_11)) shift.set(false);
    }

    @Inject(method = "breakBlock", at = @At("HEAD"))
    private void sendBlockMined(int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9)) {
            networkHandler.sendPacket(new PlayerActionC2SPacket(3, i, j, k, l));
        }
    }

    @Inject(method = "attackBlock", at = @At(value = "HEAD"), cancellable = true)
    private void startMining(int i, int j, int k, int l, CallbackInfo ci) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9)) {
            breakingBlock = true;
            networkHandler.sendPacket(new PlayerActionC2SPacket(0, i, j, k, l));
            int id = minecraft.world.getBlockId(i, j, k);
            if (id > 0 && blockBreakingProgress == 0.0F) {
                Block.BLOCKS[id].onBlockBreakStart(minecraft.world, i, j, k, minecraft.player);
            }
            if (id > 0 && Block.BLOCKS[id].getHardness(this.minecraft.player) >= 1.0F) {
                this.breakBlock(i, j, k, l);
            }
            ci.cancel();
        }
    }

    @Inject(method = "cancelBlockBreaking", at = @At("HEAD"))
    private void stopMining(CallbackInfo ci) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9) && breakingBlock) {
            networkHandler.sendPacket(new PlayerActionC2SPacket(2, 0, 0, 0, 0));
            breakingDelayTicks = 0;
        }
    }

    @Inject(method = "processBlockBreakingAction", at = @At("HEAD"))
    private void sendMining(int i, int j, int k, int l, CallbackInfo ci) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9)) {
            breakingBlock = true;
            networkHandler.sendPacket(new PlayerActionC2SPacket(1, i, j, k, l));
        }
    }

    @Redirect(method = "processBlockBreakingAction", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MultiplayerInteractionManager;breakingBlock:Z",
            opcode = Opcodes.PUTFIELD), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I")))
    private void redirectPutField_2615(MultiplayerInteractionManager instance, boolean b) {
        if (!ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9)) breakingBlock = b;
    }

    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/MultiplayerInteractionManager;breakingBlock:Z",
                    opcode = Opcodes.PUTFIELD, ordinal = 1)))
    private void redirectSendStopMiningPacket(ClientNetworkHandler handler, Packet packet) {
        if (!ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9)) handler.sendPacket(packet);
    }

    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MultiplayerInteractionManager;attackBlock(IIII)V"))
    private void redirectStartMiningInSendMining(MultiplayerInteractionManager manager, int i, int j, int k, int l) {
        if (ProtocolVersionManager.isBefore(ProtocolVersion.BETA_9)) {
            blockBreakingProgress = 0.0F;
            lastBlockBreakingProgress = 0.0F;
            breakingSoundDelayTicks = 0.0F;
            breakingPosX = i;
            breakingPosY = j;
            breakingPosZ = k;
        } else {
            attackBlock(i, j, k, l);
        }
    }
}
