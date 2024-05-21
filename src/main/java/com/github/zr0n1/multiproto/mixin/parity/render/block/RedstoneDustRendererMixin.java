package com.github.zr0n1.multiproto.mixin.parity.render.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRenderManager.class)
public class RedstoneDustRendererMixin {

    @Redirect(method = "renderRedstoneDust", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V", ordinal = 0))
    private void applyTintParity(Tessellator t, float r, float g, float b, @Local(name = "var8") float luminance,
                                 @Local(name = "var6") int meta) {
        ProtocolVersion version = ProtocolVersionManager.getVersion();
        if (version.compareTo(ProtocolVersion.BETA_11) < 0 && meta == 0 && Multiproto.config.textureParity) r = 0F;
        if (version.compareTo(ProtocolVersion.BETA_9) < 0 && Multiproto.config.textureParity) r = g = b = luminance;
        t.color(r, g, b);
    }
}
