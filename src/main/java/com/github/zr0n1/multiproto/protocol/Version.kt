package com.github.zr0n1.multiproto.protocol

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.event.RegisterVersionsListener
import com.github.zr0n1.multiproto.mixin.entity.EntityAccessor
import com.github.zr0n1.multiproto.protocol.Version.Type
import com.github.zr0n1.multiproto.protocol.packet.DataType
import com.github.zr0n1.multiproto.protocol.packet.FieldEntry
import com.github.zr0n1.multiproto.protocol.packet.PacketWrapper
import com.github.zr0n1.multiproto.protocol.parity.BlockJuice.Companion.absorb
import com.github.zr0n1.multiproto.protocol.parity.ToolJuice.Companion.absorb
import com.github.zr0n1.multiproto.protocol.parity.VersionParity
import com.github.zr0n1.multiproto.protocol.parity.VersionParity.Companion.addBlockTexture
import com.github.zr0n1.multiproto.protocol.parity.VersionParity.Companion.remove
import com.github.zr0n1.multiproto.protocol.parity.VersionParity.Companion.removeCraftingRecipes
import com.github.zr0n1.multiproto.protocol.parity.VersionParity.Companion.removeSmeltingRecipes
import com.github.zr0n1.multiproto.protocol.parity.VersionParity.Companion.replaceCraftingRecipe
import com.github.zr0n1.multiproto.protocol.parity.VersionParity.Companion.setTexture
import com.github.zr0n1.multiproto.protocol.parity.VersionParity.Companion.translate
import com.github.zr0n1.multiproto.util.fabric
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.network.NetworkHandler
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
import net.minecraft.network.packet.login.LoginHelloPacket
import net.minecraft.network.packet.play.EntityAnimationPacket
import net.minecraft.network.packet.play.PlayerRespawnPacket
import net.minecraft.network.packet.s2c.play.*
import net.modificationstation.stationapi.api.entity.player.PlayerHelper
import net.modificationstation.stationapi.api.item.tool.ToolLevel
import net.modificationstation.stationapi.mixin.entity.client.ClientNetworkHandlerAccessor

/**
 * Represents a Minecraft multiplayer protocol version.
 */
abstract class Version(
    /**
     * Version type.
     *
     * @see Type
     */
    val type: Type,
    /**
     * Protocol version number.
     * (Example: `14` for Beta 1.7 - Beta 1.7.3.)
     */
    @JvmField
    val version: Int,
    /**
     * Client version number.
     */
    private val client: String,
) : VersionParity, Comparable<Version> {
    abstract fun packets()

    companion object {
        private val _list: MutableList<Version> = ArrayList()
        @JvmStatic
        val LIST: List<Version> by lazy { _list.sorted() }

        fun register(version: Version) = version.also { _list += it }

        /**
         * Beta 1.7.3 (Protocol version: 14)
         */
        @JvmField
        val B1_7_3 = register(object : Version(Type.BETA, 14, "1.7.3") {
            override fun packets() {
                Protocol.registerWrapper(1) { packet: LoginHelloPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.INT, writeValue = { Protocol.version.version }), // protocol
                        FieldEntry(DataType.string(16)), // username
                        FieldEntry(DataType.LONG), // seed
                        FieldEntry(DataType.BYTE) // dimension
                    )
                }
            }
        })

        /**
         * Beta 1.6.6 (Protocol version: 13)
         */
        @JvmField
        val slabSideTextures = IntArray(4)

        @JvmField
        val B1_6_6 = register(object : Version(Type.BETA, 13, "1.6.6"), VersionParity by B1_7_3 {
            override fun packets() = B1_7_3.packets()

            override fun blocks() {
                Block.COBWEB.absorb {
                    material = Material.WOOL
                    hardness = 0F
                    opacity = 0
                }
            }

            override fun removals() = remove(Block.PISTON, Block.STICKY_PISTON, Item.SHEARS)

            override fun textures() {
                Block.BRICKS.setTexture("block/bricks")
                Block.COBBLESTONE.setTexture("block/cobblestone")
                slabSideTextures[0] = addBlockTexture("block/smooth_stone_slab_side").index
                slabSideTextures[1] = addBlockTexture("block/sandstone_slab_side").index
                slabSideTextures[2] = addBlockTexture("block/planks_slab_side").index
                slabSideTextures[3] = addBlockTexture("block/cobblestone_slab_side").index
            }
        })

        /**
         * Beta 1.5_01 (Protocol version: 11)
         */
        @JvmField
        val B1_5_01 = register(object : Version(Type.BETA, 11, "1.5_01"), VersionParity by B1_6_6 {
            override fun packets() {
                B1_6_6.packets()
                Protocol.registerWrapper(23) { packet: EntitySpawnS2CPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.INT), // id
                        FieldEntry(DataType.BYTE, 7), // entity type
                        FieldEntry(DataType.INT), // x
                        FieldEntry(DataType.INT), // y
                        FieldEntry(DataType.INT), // z
                        FieldEntry.dummy(0), // velocity x
                        FieldEntry.dummy(0), // velocity y
                        FieldEntry.dummy(0) // velocity z
                    )
                }
                Protocol.registerWrapper(9) { packet: PlayerRespawnPacket -> PacketWrapper(packet) } // no values
            }

            override fun blocks() {
                B1_6_6.blocks()
                Block.GLOWSTONE.absorb { material = Material.GLASS }
            }

            override fun removals() {
                B1_6_6.removals()
                remove(Block.DEAD_BUSH, Block.GRASS, Block.TRAPDOOR, Item.MAP)
            }
        })

        /**
         * Beta 1.4_01 (Protocol version: 10)
         */
        @JvmField
        val B1_4_01 = register(object : Version(Type.BETA, 10, "1.4_01"), VersionParity by B1_5_01 {
            override fun packets() {
                B1_5_01.packets()
                Protocol.registerWrapper(1) { packet: LoginHelloPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.INT, writeValue = { Protocol.version.version }), // protocol
                        FieldEntry(DataType.UTF), // username
                        FieldEntry.unique(DataType.UTF, "Password"),
                        FieldEntry(DataType.LONG), // seed
                        FieldEntry(DataType.BYTE) // dimension
                    )
                }
                Protocol.registerWrapper(102) { packet: ClickSlotC2SPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.BYTE), // id
                        FieldEntry(DataType.SHORT), // slot
                        FieldEntry(DataType.BYTE), // button
                        FieldEntry(DataType.SHORT), // action type
                        FieldEntry.dummy(false, 5), // shift click
                        FieldEntry(DataType.ITEM_STACK), // itemstack
                    )
                }
            }

            override fun blocks() {
                B1_5_01.blocks()
                Block.GLOWSTONE.absorb { material = Material.GLASS }
            }

            override fun removals() {
                B1_5_01.removals()
                remove(Block.COBWEB, Block.DETECTOR_RAIL, Block.POWERED_RAIL)
                replaceCraftingRecipe(
                    ItemStack(Block.LADDER, 2),
                    ItemStack(Block.LADDER, 1),
                    "# #", "###", "# #", '#', Item.STICK
                )
            }
        })

        /**
         * Beta 1.3_01 (Protocol version: 9)
         */
        @JvmField
        val B1_3_01 = register(object : Version(Type.BETA, 9, "1.3_01"), VersionParity by B1_4_01 {
            override fun packets() {
                B1_4_01.packets()
                Protocol.registerWrapper(102) { packet: ClickSlotC2SPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.BYTE), // id
                        FieldEntry(DataType.SHORT), // slot
                        FieldEntry(DataType.BYTE), // button
                        FieldEntry(DataType.SHORT), // action type
                        FieldEntry.dummy(false, 5), // shift click
                        FieldEntry(DataType.ITEM_STACK), // itemstack
                    )
                }
            }

            override fun removals() {
                B1_4_01.removals()
                remove(Item.COOKIE)
            }
        })

        /**
         * Beta 1.2_02 (Protocol version: 8) (Notch moment)
         */
        @JvmField
        val redstoneWireTextures = IntArray(2)

        @JvmField
        val B1_2_02 = register(object : Version(Type.BETA, 8, "1.2_02"), VersionParity by B1_3_01 {
            override fun packets() = B1_3_01.packets()

            override fun removals() {
                B1_3_01.removals()
                remove(Block.BED, Item.BED, Block.REPEATER, Item.REPEATER)
                removeCraftingRecipes(
                    ItemStack(Block.SLAB, 3, 3),
                    ItemStack(Block.SLAB, 3, 2),
                    ItemStack(Block.SLAB, 3, 1)
                )
                replaceCraftingRecipe(ItemStack(Block.SLAB, 3), "###", '#', Block.COBBLESTONE)
                replaceCraftingRecipe(ItemStack(Block.STONE_PRESSURE_PLATE), "###", '#', Block.STONE)
                replaceCraftingRecipe(ItemStack(Block.WOODEN_PRESSURE_PLATE), "###", '#', Block.PLANKS)
            }

            override fun textures() {
                B1_3_01.textures()
                Block.REDSTONE_WIRE.setTexture("block/redstone_dust_cross")
                redstoneWireTextures[0] = Block.REDSTONE_WIRE.textureId
                addBlockTexture("block/redstone_dust_line")
                redstoneWireTextures[1] = addBlockTexture("block/redstone_dust_cross_on").index
                addBlockTexture("block/redstone_dust_line_on")
            }

            override fun translations() = translate(Item.GUNPOWDER)
        })

        /**
         * Beta 1.1_02 (Protocol version: 8)
         */
        @JvmField
        val B1_1_02 = register(object : Version(Type.BETALPHA, 8, "1.1_02"), VersionParity by B1_2_02 {
            override fun packets() {
                B1_2_02.packets()
                Protocol.registerWrapper(5) { packet: EntityEquipmentUpdateS2CPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.INT), // entity id
                        FieldEntry(DataType.SHORT), // slot id
                        FieldEntry(DataType.SHORT), // item id
                        FieldEntry.dummy(0) // damage
                    )
                }
                Protocol.registerWrapper(15) { packet: PlayerInteractBlockC2SPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.INT),
                        FieldEntry(DataType.BYTE),
                        FieldEntry(DataType.INT),
                        FieldEntry(DataType.BYTE),
                        FieldEntry(DataType.ITEM_STACK)
                    )
                }
                Protocol.registerRedirect(19) { packet: ClientCommandC2SPacket ->
                    EntityAnimationPacket(PlayerHelper.getPlayerFromGame(), if (packet.mode == 1) 104 else 105)
                }
                Protocol.registerHandler(18) { packet: EntityAnimationPacket, handler: NetworkHandler ->
                    val e = (handler as ClientNetworkHandlerAccessor).invokeMethod_1645(packet.id)
                    if (e != null && (packet.animationId == 104 || packet.animationId == 105)) {
                        (e as EntityAccessor).invokeSetFlag(1, packet.animationId == 104) // start/stop sneaking
                    } else packet.apply(handler)
                }
                Protocol.registerWrapper(21) { packet: ItemEntitySpawnS2CPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.INT), // entity id
                        FieldEntry(DataType.SHORT, 7), // item id
                        FieldEntry(DataType.BYTE, 8), // count
                        FieldEntry.dummy(0, 9), // damage
                        FieldEntry(DataType.INT), // x
                        FieldEntry(DataType.INT), // y
                        FieldEntry(DataType.INT), // z
                        FieldEntry(DataType.BYTE), // velocity x
                        FieldEntry(DataType.BYTE), // velocity y
                        FieldEntry(DataType.BYTE) // velocity z
                    )
                }
                Protocol.registerWrapper(24) { packet: LivingEntitySpawnS2CPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.INT), // id
                        FieldEntry(DataType.BYTE), // entity type
                        FieldEntry(DataType.INT), // x
                        FieldEntry(DataType.INT), // y
                        FieldEntry(DataType.INT), // z
                        FieldEntry(DataType.BYTE), // yaw
                        FieldEntry(DataType.BYTE) // pitch
                    )
                }
                Protocol.registerWrapper(103) { packet: ScreenHandlerSlotUpdateS2CPacket ->
                    PacketWrapper(packet,
                        FieldEntry(DataType.BYTE),
                        FieldEntry(DataType.SHORT),
                        FieldEntry(DataType.ITEM_STACK)
                    )
                }
            }

            override fun items() {
                B1_2_02.items()
                Item.ITEMS.filterIsInstance<ToolLevel>().forEach {
                    it.absorb {
                        maxDamage = (32 shl material.miningLevel) * (if (material.miningLevel == 3) 4 else 1)
                        if (it is ToolItem) miningSpeed = ((material.miningLevel + 1) * 2).toFloat()
                    }
                }
            }

            override fun removals() {
                B1_2_02.removals()
                remove(
                    Block.CAKE, Item.CAKE,
                    Block.DISPENSER,
                    Block.LAPIS_BLOCK, Block.LAPIS_ORE,
                    Block.NOTE_BLOCK,
                    Block.SANDSTONE,
                    Item.BONE,
                    Item.DYE,
                    Item.SUGAR
                )
                removeSmeltingRecipes(Block.CACTUS, Block.LOG)
            }

            override fun translations() {
                B1_2_02.translations()
                translate(Block.CRAFTING_TABLE, Block.SUGAR_CANE, Item.SUGAR_CANE)
            }
        })

        /**
         * Beta 1.1_01 (Protocol version: 7)
         *
         * Identical to [B1_1_02] besides chests not opening with empty hands serverside.
         */
        @JvmField
        @Suppress("unused")
        val B1_1_01 = register(object : Version(Type.BETALPHA, 7, "1.1_01"), VersionParity by B1_2_02 {
            override fun packets() = B1_1_02.packets()
        })
//
//        /**
//         * Alpha v1.2.3_05 - Alpha v1.2.6
//         */
//        @JvmField
//        val ALPHA_6 = register(Type.ALPHA, 6, "1.2.3_05", "1.2.6")
//
//        /**
//         * Alpha v1.2.3 - Alpha v1.2.3_04
//         */
//        @JvmField
//        val ALPHA_5 = register(Type.ALPHA, 5, "1.2.3", "1.2.3_04")
//
//        /**
//         * Alpha v1.2.2
//         */
//        @JvmField
//        val ALPHA_4 = register(Type.ALPHA, 4, "1.2.2")
//
//        /**
//         * Alpha v1.2.0 - Alpha v1.2.1_01
//         */
//        @JvmField
//        val ALPHA_3 = register(Type.ALPHA, 3, "1.20", "1.2.1_01")
//
//        /**
//         * Alpha v1.1.1 - Alpha v1.1.2_01
//         */
//        @JvmField
//        val ALPHA_2 = register(Type.ALPHA, 2, "1.1.1", "1.1.2_01")

        internal fun registerAll() {
            fabric.invokeEntrypoints("multiproto:register_versions", RegisterVersionsListener::class.java) { it() }
            LIST
        }

        /**
         * @param s [String] representing a protocol versions's type and version number.
         * @return [Version] which matches the given string or [.BETA_14].
         * @see Version.toString
         */
        @JvmStatic
        fun parse(s: String?): Version {
            if (s.isNullOrBlank()) return B1_7_3
            val s1 = s.replace("\\s".toRegex(), "").replace("beta_initial", "betalpha")
            return LIST.stream().filter { it.toString().equals(s1, ignoreCase = true) }
                .findFirst().orElse(B1_7_3)
        }
    }

    @JvmOverloads
    fun name(shorten: Boolean = false): String {
        return type.getLabel(shorten) + (if (shorten) "" else " ") + (if (shorten) "" else type.prefix) + client
    }

    /**
     * @return [String] representing the [Version] by [type] and [version].
     *
     * (Example: [B1_7_3] -> `"beta_14"`)
     */
    override fun toString() = "${type.id}_$version"

    /**
     * Compares release order via type and version number.
     */
    override fun compareTo(other: Version): Int {
        return compareBy<Version> { it.type }.thenBy { it.version }.compare(this, other)
    }

    @Deprecated("JAVA UTIL", ReplaceWith("ver LE other"))
    fun isLE(other: Version) = this <= other

    @Deprecated("JAVA UTIL", ReplaceWith("ver GE other"))
    fun isGE(other: Version) = this >= other

    enum class Type(
        val id: String,
        private val label: String,
        private val abbreviation: String,
        val prefix: String = ""
    ) {
        ALPHA("alpha", "Alpha", "a", "v"),
        BETALPHA("betalpha", "Beta", "b"),
        BETA("beta", "Beta", "b");

        fun getLabel(shorten: Boolean): String = if (shorten) abbreviation else label
    }
}