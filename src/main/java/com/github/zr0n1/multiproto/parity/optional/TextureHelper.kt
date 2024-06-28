package com.github.zr0n1.multiproto.parity.optional

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.protocol.*
import com.github.zr0n1.multiproto.util.minecraft
import net.mine_diver.unsafeevents.listener.EventListener
import net.minecraft.block.Block
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas

object TextureHelper : VersionChangedListener {
    /**
     * smooth stone, sandstone, planks, cobblestone
     */
    @JvmField
    val slabSideTextures = IntArray(4)

    /**
     * cross off, cross on
     */
    @JvmField
    val redstoneWireTextures = IntArray(2)

    @EventListener
    fun registerTextures(event: TextureRegisterEvent) = this()

    override fun invoke() {
        val terrain: ExpandableAtlas = Atlases.getTerrain()
        when {
            currVer <= BETA_13 && Multiproto.config.textureParity -> {
                Block.BRICKS.textureId = terrain.addTexture(Multiproto.NAMESPACE.id("block/bricks")).index
                Block.COBBLESTONE.textureId = terrain.addTexture(Multiproto.NAMESPACE.id("block/cobblestone")).index
                slabSideTextures[0] = terrain.addTexture(Multiproto.NAMESPACE.id("block/smooth_stone_slab_side")).index
                slabSideTextures[1] = terrain.addTexture(Multiproto.NAMESPACE.id("block/sandstone_slab_side")).index
                slabSideTextures[2] = terrain.addTexture(Multiproto.NAMESPACE.id("block/planks_slab_side")).index
                slabSideTextures[3] = terrain.addTexture(Multiproto.NAMESPACE.id("block/cobblestone_slab_side")).index
            }
            currVer <= BETA_8 && Multiproto.config.textureParity -> {
                Block.REDSTONE_WIRE.textureId = terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_cross")).index
                Block.REDSTONE_WIRE.textureId = redstoneWireTextures[0]
                terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_line"))
                redstoneWireTextures[1] = terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_cross_on")).index
                terrain.addTexture(Multiproto.NAMESPACE.id("block/redstone_dust_line_on"))
            }
            else -> {
                Block.BRICKS.textureId = 7
                Block.COBBLESTONE.textureId = 16
                Block.REDSTONE_WIRE.textureId = 164
            }
        }
        if (minecraft.worldRenderer != null) minecraft.worldRenderer.method_1537()
        Multiproto.LOGGER.info("Registered version parity textures!")
    }
}
