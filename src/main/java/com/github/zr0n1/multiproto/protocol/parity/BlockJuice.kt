package com.github.zr0n1.multiproto.protocol.parity

import com.github.zr0n1.multiproto.mixin.parity.block.BlockAccessor
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.sound.BlockSoundGroup
import net.modificationstation.stationapi.api.util.Namespace

class BlockJuice private constructor(val block: Block) {
    val accessor: BlockAccessor = block as BlockAccessor
    var textureId = block.textureId
    var translationKey: String = block.translationKey
    var hardness
        get() = block.hardness
        set(value) {
            rawResistance = 0F
            block.setHardness(value)
            rawHardness = hardness
        }
    var resistance
        get() = accessor.rawResistance
        set(value) {
            block.setResistance(value)
            rawResistance = resistance
        }
    var material: Material = block.material
    private var rawHardness = hardness
    private var rawResistance = resistance
    var boundingBox = BoundingBox(block.minX, block.minY, block.minZ, block.maxX, block.maxY, block.maxZ)
    var soundGroup: BlockSoundGroup = block.soundGroup
    var particleFallSpeedModifier = block.particleFallSpeedModifier
    var slipperiness = block.slipperiness
    var tickRandomly = Block.BLOCKS_RANDOM_TICK[block.id]
    var opacity = Block.BLOCKS_LIGHT_OPACITY[block.id]
    var hasEntity = Block.BLOCKS_WITH_ENTITY[block.id]
    var luminance = Block.BLOCKS_LIGHT_LUMINANCE[block.id]
    var ignoreMetaUpdates = Block.BLOCKS_IGNORE_META_UPDATE[block.id]

    fun translate(namespace: Namespace, prefix: String? = null) {
        this.translationKey = "$namespace.${if (prefix != null) "$prefix." else ""}${block.translationKey}"
    }
    
    fun soak() {
        block.textureId = this.textureId
        accessor.setRawTranslationKey(this.translationKey)
        accessor.setRawHardness(this.rawHardness)
        accessor.rawResistance = this.rawResistance
        accessor.setMaterial(this.material)
        this.boundingBox.let {
            block.minX = it.minX
            block.minY = it.minY
            block.minZ = it.minZ
            block.maxX = it.maxX
            block.maxY = it.maxY
            block.maxZ = it.maxZ
        }
        block.soundGroup = this.soundGroup
        block.particleFallSpeedModifier = this.particleFallSpeedModifier
        block.slipperiness = this.slipperiness
        Block.BLOCKS_RANDOM_TICK[block.id] = this.tickRandomly
        Block.BLOCKS_LIGHT_OPACITY[block.id] = this.opacity
        Block.BLOCKS_WITH_ENTITY[block.id] = this.hasEntity
        Block.BLOCKS_LIGHT_LUMINANCE[block.id] = if (block.isOpaque && this.luminance == 0) 255 else this.luminance
        Block.BLOCKS_IGNORE_META_UPDATE[block.id] = this.ignoreMetaUpdates
        Block.BLOCKS_ALLOW_VISION[block.id] = !material.blocksVision()
        Block.BLOCKS_OPAQUE[block.id] = block.isOpaque
    }

    data class BoundingBox(
        val minX: Double, val minY: Double, val minZ: Double,
        val maxX: Double, val maxY: Double, val maxZ: Double
    )

    companion object {
        fun Block.squeeze() = BlockJuice(this)
        fun Block.absorb(block: BlockJuice.() -> Unit) = squeeze().apply(block).soak()
    }
}