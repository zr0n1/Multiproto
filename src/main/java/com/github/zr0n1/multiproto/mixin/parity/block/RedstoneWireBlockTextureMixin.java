package com.github.zr0n1.multiproto.mixin.parity.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.github.zr0n1.multiproto.parity.BlockParityHelper;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockTextureMixin extends Block {

    public RedstoneWireBlockTextureMixin(int id, Material material) {
        super(id, material);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void applyTextureParity(int side, int meta, CallbackInfoReturnable<Integer> cir) {
        if(ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_9) < 0 && Multiproto.config.textureParity) {
            cir.setReturnValue(meta > 0 ? BlockParityHelper.redstoneWireTextures[1] : BlockParityHelper.redstoneWireTextures[0]);
        }
    }

    @Inject(method = "getColorMultiplier", at = @At("HEAD"), cancellable = true)
    private void applyTextureParityColor(BlockView blockView, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if(ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_11) < 0 && Multiproto.config.textureParity) {
            cir.setReturnValue(super.getColorMultiplier(blockView, x, y, z));
        }
    }
}
