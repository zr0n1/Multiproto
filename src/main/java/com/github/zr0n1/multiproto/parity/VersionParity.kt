package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.parity.RecipeHelper.removeCrafting
import com.github.zr0n1.multiproto.parity.juice.BlockJuice
import com.github.zr0n1.multiproto.parity.juice.BlockJuice.Companion.squeeze
import com.github.zr0n1.multiproto.parity.juice.ItemJuice
import com.github.zr0n1.multiproto.parity.juice.ItemJuice.Companion.squeeze
import com.github.zr0n1.multiproto.protocol.Protocol
import com.github.zr0n1.multiproto.FABRIC
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.modificationstation.stationapi.api.item.ItemConvertible
import net.modificationstation.stationapi.api.util.Namespace

interface VersionParity {
    fun blocks() { }
    fun items() { }
    fun recipes() { }
    fun removals() { }
    fun textures() { }
    fun translations() { }
    fun misc() { }
    
    companion object {
        internal val BASE_BLOCK_JUICE by lazy { Block.BLOCKS.mapNotNull { it?.squeeze() }.toSet() }
        internal val BASE_ITEM_JUICE by lazy { Item.ITEMS.mapNotNull { it?.squeeze() }.toSet() }

        fun apply() {
            reset()
            Protocol.version.blocks()
            Protocol.version.items()
            Protocol.version.recipes()
            Protocol.version.removals()
            if (Multiproto.config.textureParity) Protocol.version.textures()
            if (Multiproto.config.translationParity) Protocol.version.translations()
            if (FABRIC.isModLoaded("hmifabric")) HMIHelper.refresh()
        }

        private fun reset() {
            BASE_BLOCK_JUICE.forEach(BlockJuice::soak)
            BASE_ITEM_JUICE.forEach(ItemJuice::soak)
            RecipeHelper.reset()
            TextureHelper.reset()
            resetTranslations()
            if (FABRIC.isModLoaded("hmifabric")) HMIHelper.reset()
        }

        fun remove(vararg items: ItemConvertible) {
            removeCrafting(*items)
            if (FABRIC.isModLoaded("hmifabric")) HMIHelper.removed += items.map(ItemConvertible::asItem)
        }

        @JvmStatic
        fun resetTranslations() {
            BASE_BLOCK_JUICE.forEach { it.accessor.setRawTranslationKey(it.translationKey) }
            BASE_ITEM_JUICE.forEach { it.accessor.setRawTranslationKey(it.translationKey) }
        }
        
        internal fun translate(vararg items: ItemConvertible, prefix: String? = null) =
            translate(*items, namespace = Multiproto.NAMESPACE, prefix = prefix)
        
        fun translate(vararg items: ItemConvertible, namespace: Namespace, prefix: String? = null) {
            val pfx = if (prefix != null) "$prefix." else ""
            items.forEach {
                if (it is Block)
                    it.setTranslationKey(namespace, pfx + it.translationKey.removePrefix("tile."))
                else if (it is Item)
                    it.setTranslationKey(namespace, pfx + it.translationKey.removePrefix("item."))
            }
        }
    }
}