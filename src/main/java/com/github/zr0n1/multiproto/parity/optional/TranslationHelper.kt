package com.github.zr0n1.multiproto.parity.optional

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.protocol.*
import net.minecraft.block.Block
import net.minecraft.item.Item

object TranslationHelper : VersionChangedListener {
    override fun invoke() {
        replaceLE(BETA_8, Item.GUNPOWDER, "sulphur")
        replaceLE(BETALPHA_8, Block.CRAFTING_TABLE, "workbench")
        replaceLE(BETALPHA_8, Block.SUGAR_CANE, "reeds")
        replaceLE(BETALPHA_8, Item.SUGAR_CANE, "reeds")
    }

    @Suppress("SameParameterValue")
    private fun replaceLE(target: Version, block: Block, key: String) {
        block.translationKey = if (currVer <= target && Multiproto.config.translationParity) "multiproto.$key" else key
    }

    private fun replaceLE(target: Version, item: Item, key: String) {
        item.translationKey = if (currVer <= target && Multiproto.config.translationParity) "multiproto.$key" else key
    }
}
