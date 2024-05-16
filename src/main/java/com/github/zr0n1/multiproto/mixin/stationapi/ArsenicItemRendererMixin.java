package com.github.zr0n1.multiproto.mixin.stationapi;

import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

// todo render inventory blocks darker when version < beta_13 or maybe dont Idk Lmao
@Mixin(ArsenicItemRenderer.class)
public class ArsenicItemRendererMixin {
}
