package com.github.zr0n1.multiproto.protocol.parity

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.event.VersionChangedListener
import com.github.zr0n1.multiproto.mixin.parity.hmifabric.UtilsAccessor
import com.github.zr0n1.multiproto.protocol.*
import net.glasslauncher.hmifabric.GuiOverlay
import net.glasslauncher.hmifabric.event.HMIItemListRefreshEvent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

object HMIHelper : HMIItemListRefreshEvent, VersionChangedListener {
    private val removed: ArrayList<Item> = ArrayList()

    override fun refreshItemList(stacks: ArrayList<ItemStack>) {
        stacks.removeIf { removed.contains(it.item) || (Protocol.version <= Version.B1_1_02 && it.damage > 0) }
    }

    override fun onChange() {
        if (UtilsAccessor.getAllItems() != null) {
            UtilsAccessor.setAllItems(null)
            GuiOverlay.resetItems()
        }
        Multiproto.LOGGER.info("Removed ${removed.size} entries from HowManyItems-Fabric")
    }
}
