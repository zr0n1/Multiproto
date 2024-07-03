@file:JvmName("Util")

package com.github.zr0n1.multiproto

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft

val FABRIC: FabricLoader
    @JvmName("getFabric")
    get() = FabricLoader.getInstance()

@Suppress("DEPRECATION")
val MINECRAFT: Minecraft
    @JvmName("getMinecraft")
    get() = FabricLoader.getInstance().gameInstance as Minecraft