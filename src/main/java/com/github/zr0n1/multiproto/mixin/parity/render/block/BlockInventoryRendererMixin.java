package com.github.zr0n1.multiproto.mixin.parity.render.block;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRenderManager.class)
public class BlockInventoryRendererMixin {

    @Shadow public boolean inventoryColorEnabled;

    @Redirect(method = "render(Lnet/minecraft/block/Block;IF)V", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/render/block/BlockRenderManager;inventoryColorEnabled:Z"))
    private boolean applyInventoryRenderColorParity(BlockRenderManager instance) {
        return (ProtocolVersionManager.getVersion().compareTo(ProtocolVersion.BETA_13) >= 0 && inventoryColorEnabled) || !Multiproto.config.blockItemRenderParity;
    }
}
