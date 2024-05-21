package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockTextureMixin extends Block {

    public LeavesBlockTextureMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void applyTextureParity(CallbackInfoReturnable<Integer> cir) {
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) {
            cir.setReturnValue(textureId);
        }
    }

    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void applyTextureParityColor(CallbackInfoReturnable<Integer> cir) {
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) {
            cir.setReturnValue(FoliageColors.getDefaultColor());
        }
    }

    @Inject(method = "getColorMultiplier", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/BlockView;getBlockMeta(III)I", shift = At.Shift.AFTER), cancellable = true)
    private void applyTextureParityColorMultiplier(BlockView blockView, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_8) < 0) {
            blockView.method_1781().method_1788(x, z, 1, 1);
            double temperature = blockView.method_1781().field_2235[0];
            double humidity = blockView.method_1781().field_2236[0];
            cir.setReturnValue(FoliageColors.getColor(temperature, humidity));
        }
    }
}
