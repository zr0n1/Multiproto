package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.Multiproto
import net.mine_diver.unsafeevents.listener.EventListener
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases
import net.modificationstation.stationapi.api.util.Namespace

object TextureHelper {
    var oldBricks: Int = 0
    var oldCobble: Int = 0
    @JvmField
    val slabSides = IntArray(4)
    @JvmField
    val redstoneWire = IntArray(2)

    @Suppress("unused", "UNUSED_PARAMETER")
    @EventListener
    fun register(event: TextureRegisterEvent) {
        oldBricks = blockTextureIndex("block/bricks")
        oldCobble = blockTextureIndex("block/cobblestone")
        redstoneWire[0] = blockTextureIndex("block/redstone_dust_cross")
        addBlockTexture("block/redstone_dust_line")
        redstoneWire[1] = blockTextureIndex("block/redstone_dust_cross_on")
        addBlockTexture("block/redstone_dust_line_on")
        slabSides[0] = blockTextureIndex("block/smooth_stone_slab_side")
        slabSides[1] = blockTextureIndex("block/sandstone_slab_side")
        slabSides[2] = blockTextureIndex("block/planks_slab_side")
        slabSides[3] = blockTextureIndex("block/cobblestone_slab_side")
    }

    @JvmStatic
    fun reset() {
        VersionParity.BASE_BLOCK_JUICE.forEach { it.block.textureId = it.textureId }
        VersionParity.BASE_ITEM_JUICE.forEach { it.item.setTextureId(it.textureId) }
    }

    internal fun blockTextureIndex(id: String) = blockTextureIndex(Multiproto.NAMESPACE, id)

    fun blockTextureIndex(namespace: Namespace, id: String) = addBlockTexture(namespace, id).index

    internal fun addBlockTexture(id: String) = addBlockTexture(Multiproto.NAMESPACE, id)

    fun addBlockTexture(namespace: Namespace, id: String): Atlas.Sprite =
        Atlases.getTerrain().addTexture(namespace.id(id))

    @Suppress("unused")
    internal fun addItemTexture(id: String): Atlas.Sprite =
        Atlases.getGuiItems().addTexture(Multiproto.NAMESPACE.id(id))

    @Suppress("unused")
    fun addItemTexture(namespace: Namespace, id: String): Atlas.Sprite =
        Atlases.getGuiItems().addTexture(namespace.id(id))
}