package farn.multiproto.mixin;

import farn.multiproto.UberBukkitClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    private Minecraft client;

    @Inject(method="renderFrame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBlendFunc(II)V", shift = At.Shift.BEFORE))
    public void multiproto_renderMpBlockBreak(CallbackInfo ci) {
        UberBukkitClientHandler.render(this.client);
    }
}
