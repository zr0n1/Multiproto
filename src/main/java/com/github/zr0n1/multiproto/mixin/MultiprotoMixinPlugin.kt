package com.github.zr0n1.multiproto.mixin

import com.github.zr0n1.multiproto.util.fabric
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo
import pl.telvarost.mojangfixstationapi.Config

class MultiprotoMixinPlugin : IMixinConfigPlugin {
    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        return when (mixinClassName) {
            "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.DirectConnectScreenMixin",
            "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.EditServerScreenMixin",
            "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.MultiplayerScreenMixin",
            "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.MultiplayerServerListWidgetMixin",
            "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.ServerDataMixin",
            "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.DirectConnectScreenAccessor",
            "com.github.zr0n1.multiproto.mixin.mojangfixstationapi.gui.EditServerScreenAccessor" ->
                shouldApplyMojangFixStAPIMixins()
            "com.github.zr0n1.multiproto.mixin.gui.MultiplayerScreen" -> !shouldApplyMojangFixStAPIMixins()
            "com.github.zr0n1.multiproto.mixin.parity.hmifabric.UtilsAccessor" -> fabric.isModLoaded("hmifabric")
            else -> true
        }
    }

    companion object {
        @JvmStatic
        fun shouldApplyMojangFixStAPIMixins(): Boolean {
            return fabric.isModLoaded("mojangfixstationapi") && Config.config.enableMultiplayerServerChanges
        }
    }

    // boiled plate
    override fun onLoad(mixinPackage: String) { }
    override fun getRefMapperConfig(): String? = null
    override fun acceptTargets(myTargets: Set<String>, otherTargets: Set<String>) { }
    override fun getMixins(): List<String>? = null
    override fun preApply(targetClassName: String, targetClass: ClassNode, mixinClassName: String, mixinInfo: IMixinInfo) { }
    override fun postApply(targetClassName: String, targetClass: ClassNode, mixinClassName: String, mixinInfo: IMixinInfo) { }
}
