package com.github.zr0n1.multiproto.protocol.parity

import com.github.zr0n1.multiproto.mixin.parity.item.ToolItemAccessor
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.modificationstation.stationapi.api.item.tool.ToolLevel

class ToolJuice internal constructor(tool: ToolLevel) : ItemJuice(tool as Item) {
    val toolAccessor: ToolItemAccessor? = tool as? ToolItemAccessor
    var material: ToolMaterial = tool.getMaterial(ItemStack(item))
    var miningSpeed: Float = toolAccessor?.miningSpeed ?: 0F
    var damage: Int = toolAccessor?.damage ?: 0
    var effectiveOnBlocks: Array<Block> = toolAccessor?.effectiveOnBlocks ?: emptyArray()

    override fun soak() {
        super.soak()
        toolAccessor?.miningSpeed = this.miningSpeed
        toolAccessor?.damage = this.damage
        toolAccessor?.effectiveOnBlocks = this.effectiveOnBlocks
    }

    companion object {
        fun ToolLevel.absorb(block: ToolJuice.() -> Unit) = ((this as Item).squeeze() as ToolJuice).apply(block).soak()
    }
}