package com.github.zr0n1.multiproto.parity

import net.minecraft.item.ItemStack
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.CraftingRecipeManager
import net.minecraft.recipe.SmeltingRecipeManager
import net.modificationstation.stationapi.api.item.ItemConvertible
import net.modificationstation.stationapi.api.recipe.CraftingRegistry

object RecipeHelper {
    internal val BASE_CRAFTING: List<*> by lazy { CraftingRecipeManager.getInstance().recipes.toList() }
    internal val BASE_SMELTING: Map<*, *> by lazy { SmeltingRecipeManager.getInstance().recipes.toMap() }

    fun reset() {
        CraftingRecipeManager.getInstance().recipes.clear()
        CraftingRecipeManager.getInstance().recipes.addAll(BASE_CRAFTING)
        SmeltingRecipeManager.getInstance().recipes.clear()
        SmeltingRecipeManager.getInstance().recipes += BASE_SMELTING
    }

    fun replaceCrafting(output: ItemStack, vararg recipe: Any) = replaceCrafting(output, output, *recipe)

    fun replaceCrafting(oldOutput: ItemStack, output: ItemStack, vararg recipe: Any) {
        removeCrafting(oldOutput)
        CraftingRegistry.addShapedRecipe(output, *recipe)
    }

    fun removeCrafting(vararg items: ItemConvertible) = items.forEach {
        CraftingRecipeManager.getInstance().recipes.removeIf { recipe ->
            (recipe as CraftingRecipe).output.itemId == it.asItem().id
        }
    }

    fun removeCrafting(vararg outputs: ItemStack) = outputs.forEach {
        CraftingRecipeManager.getInstance().recipes.removeIf { recipe ->
            (recipe as CraftingRecipe).output == it
        }
    }

    @Suppress("SameParameterValue")
    fun removeSmelting(vararg inputs: ItemConvertible) = inputs.forEach {
        SmeltingRecipeManager.getInstance().recipes.remove(it.asItem().id)
    }
}