package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.mixin.parity.hmifabric.UtilsAccessor
import com.github.zr0n1.multiproto.protocol.*
import net.glasslauncher.hmifabric.GuiOverlay
import net.glasslauncher.hmifabric.event.HMIItemListRefreshEvent
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.modificationstation.stationapi.api.item.ItemConvertible

object HMIFabricIntegrationHelper : HMIItemListRefreshEvent, VersionChangedListener {
    private val removed: ArrayList<Item> = ArrayList()

    override fun refreshItemList(stacks: ArrayList<ItemStack>) {
        stacks.removeIf { removed.contains(it.item) || (currVer <= BETALPHA_8 && it.damage > 0) }
    }

    override fun invoke() {
        removed.clear()
        // < b1.7
        addedIn(BETA_14, Block.PISTON, Block.STICKY_PISTON, Item.SHEARS)
        // < b1.6
        addedIn(BETA_13, Block.DEAD_BUSH, Block.GRASS, Block.TRAPDOOR, Item.MAP)
        // < b1.5
        addedIn(BETA_11, Block.COBWEB, Block.DETECTOR_RAIL, Block.POWERED_RAIL)
        // < b1.4
        addedIn(BETA_10, Item.COOKIE)
        // < b1.3
        addedIn(BETA_9, Block.BED, Item.BED, Block.REPEATER, Item.REPEATER)
        // < b1.2
        addedIn(BETA_8,
            Block.CAKE, Item.CAKE,
            Block.DISPENSER,
            Block.LAPIS_BLOCK, Block.LAPIS_ORE,
            Block.NOTE_BLOCK,
            Block.SANDSTONE,
            Item.BONE,
            Item.DYE,
            Item.SUGAR
        )
        if (UtilsAccessor.getAllItems() != null) {
            UtilsAccessor.setAllItems(null)
            GuiOverlay.resetItems()
        }
        Multiproto.LOGGER.info("Removed ${removed.size} entries from HowManyItems-Fabric")
    }

    private fun addedIn(target: Version, vararg items: ItemConvertible) = items.forEach {
        if (currVer < target) removed.add(it.asItem())
    }
}
