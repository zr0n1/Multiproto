package com.github.zr0n1.multiproto.protocol.parity

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.protocol.parity.BlockJuice.Companion.absorb
import com.github.zr0n1.multiproto.protocol.parity.ItemJuice.Companion.absorb
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.CraftingRecipeManager
import net.minecraft.recipe.SmeltingRecipeManager
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases
import net.modificationstation.stationapi.api.item.ItemConvertible
import net.modificationstation.stationapi.api.recipe.CraftingRegistry
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
        fun remove(vararg items: ItemConvertible) {
            removeCraftingRecipes(*items)
            items.forEach {
                if (it is Block) Block.BLOCKS[it.id] = null
                Item.ITEMS[it.asItem().id] = null
            }
        }
        
        fun replaceCraftingRecipe(output: ItemStack, vararg recipe: Any) =
            replaceCraftingRecipe(output, output, *recipe)
        
        fun replaceCraftingRecipe(oldOutput: ItemStack, output: ItemStack, vararg recipe: Any) {
            removeCraftingRecipes(oldOutput)
            CraftingRegistry.addShapedRecipe(output, *recipe)
        }
        
        fun removeCraftingRecipes(vararg items: ItemConvertible) = items.forEach {
            CraftingRecipeManager.getInstance().recipes.removeIf { recipe ->
                (recipe as CraftingRecipe).output.itemId == it.asItem().id
            }
        }
        
        fun removeCraftingRecipes(vararg outputs: ItemStack) = outputs.forEach {
            CraftingRecipeManager.getInstance().recipes.removeIf { recipe ->
                (recipe as CraftingRecipe).output == it
            }
        }

        @Suppress("SameParameterValue")
        fun removeSmeltingRecipes(vararg inputs: ItemConvertible) = inputs.forEach {
            SmeltingRecipeManager.getInstance().recipes.remove(it.asItem().id)
        }
        
        internal fun translate(vararg items: ItemConvertible, prefix: String? = null) =
            translate(*items, namespace = Multiproto.NAMESPACE, prefix = prefix)
        
        fun translate(vararg items: ItemConvertible, namespace: Namespace, prefix: String? = null) {
            items.forEach {
                if (it is Block) it.absorb { translate(namespace, prefix) }
                if (it is Item) it.absorb { translate(namespace, prefix) }
            }
        }

        internal fun Block.setTexture(id: String) = setTexture(Multiproto.NAMESPACE, id)
        
        fun Block.setTexture(namespace: Namespace, id: String): Block {
            this.textureId = addBlockTexture(namespace, id).index
            return this
        }
        
        internal fun addBlockTexture(id: String) = addBlockTexture(Multiproto.NAMESPACE, id)
        
        fun addBlockTexture(namespace: Namespace, id: String): Atlas.Sprite =
            Atlases.getTerrain().addTexture(namespace.id(id))

        @Suppress("unused")
        fun Item.setTexture(namespace: Namespace, id: String) {
            this.setTextureId(addItemTexture(namespace, id).index)
        }
        
        fun addItemTexture(namespace: Namespace, id: String): Atlas.Sprite =
            Atlases.getGuiItems().addTexture(namespace.id(id))
    }
}