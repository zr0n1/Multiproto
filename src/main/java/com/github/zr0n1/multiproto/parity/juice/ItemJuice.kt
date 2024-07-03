package com.github.zr0n1.multiproto.parity.juice

import com.github.zr0n1.multiproto.mixin.parity.item.ItemAccessor
import net.minecraft.item.Item
import net.modificationstation.stationapi.api.item.tool.ToolLevel

open class ItemJuice protected constructor(val item: Item) {
    val accessor = item as ItemAccessor
    var maxCount: Int = item.maxCount
    var maxDamage: Int = item.maxDamage
    var textureId: Int = accessor.textureId
    var isHandheld: Boolean = item.isHandheld
    var hasSubtypes: Boolean = item.hasSubtypes()
    var craftingReturnItem: Item? = item.craftingReturnItem
    var translationKey: String? = item.translationKey

    open fun soak() {
        item.maxCount = this.maxCount
        item.maxDamage = this.maxDamage
        item.setTextureId(this.textureId)
        accessor.setRawTranslationKey(this.translationKey)
        accessor.setHandheld(this.isHandheld)
        item.setHasSubtypes(this.hasSubtypes)
        if (item.maxCount == 1) item.craftingReturnItem = this.craftingReturnItem
    }

    companion object {
        fun Item.squeeze() = if (this is ToolLevel) ToolJuice(this) else ItemJuice(this)
        fun Item.absorb(block: ItemJuice.() -> Unit) = squeeze().apply(block).soak()
    }

}