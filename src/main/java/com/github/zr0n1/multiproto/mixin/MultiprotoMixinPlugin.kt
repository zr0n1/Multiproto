package com.github.zr0n1.multiproto.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MultiprotoMixinPlugin implements IMixinConfigPlugin {

    public static boolean shouldApplyMojangFixStAPIServerListIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && pl.telvarost.mojangfixstationapi.Config.config.enableMultiplayerServerChanges;
    }

    public static boolean shouldApplyMojangFixStAPIDebugScreenIntegration() {
        return FabricLoader.getInstance().isModLoaded("mojangfixstationapi") && pl.telvarost.mojangfixstationapi.Config.config.enableDebugMenuWorldSeed;
    }

    public static boolean shouldApplyHMIFabricIntegration() {
        return FabricLoader.getInstance().isModLoaded("hmifabric");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch (mixinClassName) {
            case "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.DirectConnectScreenMixin",
                 "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.EditServerScreenMixin",
                 "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.MultiplayerScreenMixin",
                 "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.MultiplayerServerListWidgetMixin",
                 "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.ServerDataMixin",
                 "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.DirectConnectScreenAccessor",
                 "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.EditServerScreenAccessor" ->
                    shouldApplyMojangFixStAPIServerListIntegration();
            case "com.github.zr0n1.multiproto.mixin.gui.MultiplayerScreen" ->
                    !shouldApplyMojangFixStAPIServerListIntegration();
            case "com.github.zr0n1.multiproto.mixin.parity.hmifabric.UtilsAccessor" ->
                    shouldApplyHMIFabricIntegration();
            default -> true;
        };
    }

    // boiled plate
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
