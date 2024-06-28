package com.github.zr0n1.multiproto.mixin.parity.optional.render.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.github.zr0n1.multiproto.protocol.ProtocolKt.*;

@Mixin(BlockRenderManager.class)
public abstract class RedstoneDustTintRendererMixin {

    @Redirect(method = "renderRedstoneDust", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V", ordinal = 0))
    @SuppressWarnings("deprecation")
    private void multiproto_applyTintParity(Tessellator t, float r, float g, float b,
                                            @Local(name = "var8") float luminance, @Local(name = "var6") int meta) {
        if (getCurrVer().isLE(BETA_10) && meta == 0 && Multiproto.config.textureParity) r = 0F;
        if (getCurrVer().isLE(BETA_8) && Multiproto.config.textureParity) r = g = b = luminance;
        t.color(r, g, b);
    }
}
