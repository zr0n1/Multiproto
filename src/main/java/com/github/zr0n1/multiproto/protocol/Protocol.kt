package com.github.zr0n1.multiproto.protocol

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.event.VersionChangedListener
import com.github.zr0n1.multiproto.mixin.network.PacketAccessor
import com.github.zr0n1.multiproto.protocol.parity.BlockJuice.Companion.squeeze
import com.github.zr0n1.multiproto.protocol.parity.ItemJuice.Companion.squeeze
import com.github.zr0n1.multiproto.protocol.packet.PacketWrapper
import com.github.zr0n1.multiproto.protocol.parity.BlockJuice
import com.github.zr0n1.multiproto.protocol.parity.HMIHelper
import com.github.zr0n1.multiproto.protocol.parity.ItemJuice
import com.github.zr0n1.multiproto.util.fabric
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.network.NetworkHandler
import net.minecraft.network.packet.Packet
import net.minecraft.recipe.CraftingRecipeManager
import net.minecraft.recipe.SmeltingRecipeManager
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Suppress("unchecked_cast")
object Protocol {
    private val listeners =
        fabric.getEntrypoints("multiproto:version_changed", VersionChangedListener::class.java).toMutableList()
            .also { if (fabric.isModLoaded("hmifabric")) it += HMIHelper }.toList()

    /**
     * Current protocol version.
     */
    @JvmStatic
    @get:JvmName("getVer")
    var version: Version = Version.B1_7_3
        @JvmName("setVer")
        set(value) {
            if (field != value) {
                field = value
                reset()
                field.packets()
                field.blocks()
                field.items()
                field.recipes()
                field.removals()
                if (Multiproto.config.textureParity) field.textures()
                if (Multiproto.config.translationParity) field.translations()
                listeners.forEach(VersionChangedListener::onChange)
            }
        }

    private val VANILLA_CLIENTBOUND_IDS: Set<Int> by lazy { PacketAccessor.getClientBoundPackets().toMutableSet() }
    private val VANILLA_SERVERBOUND_IDS: Set<Int> by lazy { PacketAccessor.getServerBoundPackets().toMutableSet() }
    private val replacements: MutableMap<Int, KClass<Packet>> = HashMap()
    private val wrappers: MutableMap<Int, (Packet) -> PacketWrapper<out Packet>> = HashMap()
    private val redirects: MutableMap<Int, (Packet) -> Packet> = HashMap()
    private val handlers: MutableMap<Int, (Packet, NetworkHandler) -> Unit> = HashMap()
    private lateinit var clientBoundIds: MutableSet<Int>
    private lateinit var serverBoundIds: MutableSet<Int>

    private val BASE_CRAFTING_RECIPES: List<*> by lazy { CraftingRecipeManager.getInstance().recipes.toList() }
    private val BASE_SMELTING_RECIPES: Map<*, *> by lazy { SmeltingRecipeManager.getInstance().recipes.toMap() }
    private val BASE_BLOCK_JUICE by lazy { Block.BLOCKS.mapNotNull { it?.squeeze() }.toSet() }
    private val BASE_ITEM_JUICE by lazy { Item.ITEMS.mapNotNull { it?.squeeze() }.toSet() }

    @JvmStatic
    fun wrap(packet: Packet): Packet? = wrappers[packet.rawId]?.invoke(packet).also { it?.wrapperId = packet.rawId }

    @JvmStatic
    fun redirect(packet: Packet) = redirects[packet.rawId]?.invoke(packet)

    @JvmStatic
    fun replace(id: Int) = replacements[id]?.primaryConstructor?.call()

    @JvmStatic
    fun handle(packet: Packet, handler: NetworkHandler) = handlers[packet.rawId]?.invoke(packet, handler)

    @JvmStatic
    fun hasWrapper(id: Int) = id in wrappers

    @JvmStatic
    fun hasRedirect(id: Int) = redirects.containsKey(id)

    @JvmStatic
    fun isReplaced(id: Int) = replacements.containsKey(id)

    @JvmStatic
    fun hasApplier(id: Int) = id in handlers

    fun reset() {
        wrappers.clear()
        redirects.clear()
        replacements.clear()
        handlers.clear()
        clientBoundIds = VANILLA_CLIENTBOUND_IDS.toMutableSet()
        serverBoundIds = VANILLA_SERVERBOUND_IDS.toMutableSet()
        CraftingRecipeManager.getInstance().recipes.clear()
        CraftingRecipeManager.getInstance().recipes.addAll(BASE_CRAFTING_RECIPES)
        SmeltingRecipeManager.getInstance().recipes.clear()
        SmeltingRecipeManager.getInstance().recipes += BASE_SMELTING_RECIPES
        BASE_BLOCK_JUICE.forEach(BlockJuice::soak)
        BASE_ITEM_JUICE.forEach(ItemJuice::soak)
        resetTextures()
        resetTranslations()
    }

    @JvmStatic
    fun resetTextures() {
        BASE_BLOCK_JUICE.forEach { it.block.textureId = it.textureId }
        BASE_ITEM_JUICE.forEach { it.item.setTextureId(it.textureId) }
    }

    @JvmStatic
    fun resetTranslations() {
        BASE_BLOCK_JUICE.forEach { it.accessor.setRawTranslationKey(it.translationKey) }
        BASE_ITEM_JUICE.forEach { it.accessor.setRawTranslationKey(it.translationKey) }
    }

    @Suppress("unused")
    fun register(id: Int, replacement: KClass<Packet>) {
        replacements[id] = replacement
    }

    fun <T : Packet> registerWrapper(id: Int, wrapper: (T) -> PacketWrapper<T>) {
        wrappers[id] = wrapper as (Packet) -> PacketWrapper<out Packet>
    }

    fun <T : Packet> registerRedirect(id: Int, redirect: (T) -> Packet) {
        redirects[id] = redirect as (Packet) -> Packet
    }

    fun <T : Packet> registerHandler(id: Int, handler: (T, NetworkHandler) -> Unit) {
        handlers[id] = handler as (Packet, NetworkHandler) -> Unit
    }
}
