package com.github.zr0n1.multiproto.protocol.packet
//
//import com.github.zr0n1.multiproto.event.VersionChangedListener
//import com.github.zr0n1.multiproto.protocol.*
//import net.minecraft.item.ItemStack
//import net.minecraft.network.NetworkHandler
//import net.minecraft.screen.PlayerScreenHandler
//import net.modificationstation.stationapi.api.entity.player.PlayerHelper
//
//internal object AlphaPackets : VersionChangedListener {
//    override fun invoke() {
//        // <= a1.2.6
//        Protocol.replaceLE(Version.ALPHA_6, 5) {
//            PacketWrapper(object {
//                var type: Int = 0
//                lateinit var stacks: Array<ItemStack>
//            }, apply = { data, _: NetworkHandler? ->
//                val player = PlayerHelper.getPlayerFromGame()
//                when (data.type) {
//                    -1 -> player.inventory.main = data.stacks
//                    -2 -> data.stacks.forEachIndexed { i, stack ->
//                        (player.container as PlayerScreenHandler).craftingInput.setStack(i, stack)
//                    }
//                    -3 -> player.inventory.armor = data.stacks
//                }
//            }).infer()
//        }
//    }
//}
