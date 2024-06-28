package com.github.zr0n1.multiproto.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft

val fabric: FabricLoader by lazy { FabricLoader.getInstance() }
@Suppress("DEPRECATION")
val minecraft: Minecraft by lazy { FabricLoader.getInstance().gameInstance as Minecraft }