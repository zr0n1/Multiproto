package com.github.zr0n1.multiproto.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft

val fabric: FabricLoader
    get() = FabricLoader.getInstance()
val minecraft: Minecraft
    @Suppress("DEPRECATION")
    get() = FabricLoader.getInstance().gameInstance as Minecraft