package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.mixin.parity.item.ToolItemAccessor
import com.github.zr0n1.multiproto.protocol.*
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.item.ToolMaterial
import net.modificationstation.stationapi.api.item.tool.ToolLevel

object ItemHelper : VersionChangedListener {
    override fun invoke() {
        // set tool durability and speed lower <= b1.1_02
        Item.ITEMS.filter { it is ToolLevel }.forEach {
            val material: ToolMaterial = (it as ToolLevel).getMaterial(ItemStack(it))
            it.setMaxDamage(
                if (currVer <= BETALPHA_8) {
                    (32 shl material.miningLevel) * (if (material.miningLevel == 3) 4 else 1)
                } else material.durability
            )
            if (it is ToolItem) {
                (it as ToolItemAccessor).setMiningSpeed(
                    if (currVer <= BETALPHA_8) {
                        ((material.miningLevel + 1) * 2).toFloat()
                    } else material.miningSpeedMultiplier
                )
            }
        }
        Multiproto.LOGGER.info("Applied version item parity")
    }
}
