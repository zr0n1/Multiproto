package com.github.zr0n1.multiproto.parity

import com.github.zr0n1.multiproto.Multiproto
import com.github.zr0n1.multiproto.api.event.VersionChangedListener
import com.github.zr0n1.multiproto.protocol.*
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.CraftingRecipeManager
import net.minecraft.recipe.SmeltingRecipeManager
import net.modificationstation.stationapi.api.recipe.CraftingRegistry

object RecipeHelper : VersionChangedListener {
    private var vanillaCraftingRecipes: List<*>? = null
    private var vanillaSmeltingRecipes: Map<*, *>? = null

    /**
     * Handles recipe changes between versions.
     */
    override fun invoke() {
        if (vanillaCraftingRecipes == null) vanillaCraftingRecipes = CraftingRecipeManager.getInstance().recipes.toList()
        if (vanillaSmeltingRecipes == null) vanillaSmeltingRecipes = SmeltingRecipeManager.getInstance().recipes.toMap()
        CraftingRecipeManager.getInstance().recipes.clear()
        CraftingRecipeManager.getInstance().recipes.addAll(vanillaCraftingRecipes!!)
        SmeltingRecipeManager.getInstance().recipes.clear()
        SmeltingRecipeManager.getInstance().recipes.putAll(vanillaSmeltingRecipes!!)
        // < b1.7
        addedIn(BETA_14,
            ItemStack(Block.PISTON), ItemStack(Block.STICKY_PISTON),
            ItemStack(Item.SHEARS)
        )
        // < b1.6
        addedIn(BETA_13,
            ItemStack(Block.TRAPDOOR, 2),
            ItemStack(Item.MAP, 1)
        )
        // < b1.5
        addedIn(BETA_11,
            ItemStack(Block.POWERED_RAIL, 6), ItemStack(Block.DETECTOR_RAIL, 6)
        )
        replaceLE(BETA_10,
            ItemStack(Block.LADDER, 2), ItemStack(Block.LADDER, 1), "# #", "###", "# #", '#', Item.STICK
        )
        // < b1.4
        addedIn(BETA_10, ItemStack(Item.COOKIE, 8))
        // < b1.3
        addedIn(
            BETA_9,
            ItemStack(Item.BED),
            ItemStack(Item.REPEATER),
            ItemStack(Block.SLAB, 3, 3),
            ItemStack(Block.SLAB, 3, 2),
            ItemStack(Block.SLAB, 3, 1)
        )
        replaceLE(BETA_9, ItemStack(Block.SLAB, 3), "###", '#', Block.COBBLESTONE)
        replaceLE(BETA_9, ItemStack(Block.STONE_PRESSURE_PLATE), "###", '#', Block.STONE)
        replaceLE(BETA_9, ItemStack(Block.WOODEN_PRESSURE_PLATE), "###", '#', Block.PLANKS)
        // < b1.2
        addedIn(BETA_8,
            ItemStack(Item.CAKE),
            ItemStack(Block.DISPENSER),
            ItemStack(Block.NOTE_BLOCK),
            ItemStack(Block.SANDSTONE),
            ItemStack(Item.SUGAR)
        )
        for (i in 0..15) {
            addedIn(BETA_8, ItemStack(Block.WOOL, 1, i.inv() and 15), ItemStack(Item.DYE, 1, i))
        }
        if (currVer < BETA_8) {
            SmeltingRecipeManager.getInstance().recipes.remove(Block.CACTUS.id)
            SmeltingRecipeManager.getInstance().recipes.remove(Block.LOG.id)
        }
        Multiproto.LOGGER.info("Applied version recipe parity")
    }

    @Suppress("SameParameterValue")
    private fun replaceLE(target: Version, output: ItemStack, vararg recipe: Any) {
        replaceLE(target, output, output, *recipe)
    }

    private fun replaceLE(target: Version, oldOutput: ItemStack, output: ItemStack = oldOutput, vararg recipe: Any) {
        addedIn(target, oldOutput)
        CraftingRegistry.addShapedRecipe(output, *recipe)
    }

    private fun addedIn(target: Version, vararg outputs: ItemStack) {
        if (currVer < target) {
            outputs.forEach {
                CraftingRecipeManager.getInstance().recipes.removeIf {
                    recipe -> (recipe as CraftingRecipe).output == it
                }
            }
        }
    }
}
