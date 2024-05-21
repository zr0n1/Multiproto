package com.github.zr0n1.multiproto.parity;

import com.github.zr0n1.multiproto.Multiproto;
import com.github.zr0n1.multiproto.protocol.ProtocolVersion;
import com.github.zr0n1.multiproto.protocol.ProtocolVersionManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.SmeltingRecipeManager;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeParityHelper {

    public static List vanillaCraftingRecipes = new ArrayList();
    public static Map vanillaSmeltingRecipes = new HashMap();

    /**
     * Handles recipe changes between versions.
     */
    public static void applyParity() {
        ProtocolVersion v = ProtocolVersionManager.getVersion();
        CraftingRecipeManager.getInstance().getRecipes().clear();
        CraftingRecipeManager.getInstance().getRecipes().addAll(vanillaCraftingRecipes);
        SmeltingRecipeManager.getInstance().getRecipes().clear();
        SmeltingRecipeManager.getInstance().getRecipes().putAll(vanillaSmeltingRecipes);
        // < Beta 1.7
        if (v.compareTo(ProtocolVersion.BETA_14) < 0) {
            removeCraftingRecipes(new ItemStack(Block.PISTON), new ItemStack(Block.STICKY_PISTON), new ItemStack(Item.SHEARS));
        }
        // < Beta 1.6
        if (v.compareTo(ProtocolVersion.BETA_13) < 0) {
            removeCraftingRecipes(new ItemStack(Block.TRAPDOOR, 2), new ItemStack(Item.MAP, 1));
        }
        // < Beta 1.5
        if (v.compareTo(ProtocolVersion.BETA_11) < 0) {
            removeCraftingRecipes(new ItemStack(Block.POWERED_RAIL, 6), new ItemStack(Block.DETECTOR_RAIL, 6));
            replaceCraftingRecipe(new ItemStack(Block.LADDER, 2), new ItemStack(Block.LADDER, 1),
                    "# #", "###", "# #", '#', Item.STICK);
        }
        // < Beta 1.4
        if (v.compareTo(ProtocolVersion.BETA_10) < 0) {
            removeCraftingRecipes(new ItemStack(Item.COOKIE, 8));
        }
        // < Beta 1.3
        if (v.compareTo(ProtocolVersion.BETA_9) < 0) {
            removeCraftingRecipes(new ItemStack(Item.BED), new ItemStack(Item.REPEATER), new ItemStack(Block.SLAB, 3, 3),
                    new ItemStack(Block.SLAB, 3, 2), new ItemStack(Block.SLAB, 3, 1));
            replaceCraftingRecipe(new ItemStack(Block.SLAB, 3), "##", '#', Block.STONE);
            replaceCraftingRecipe(new ItemStack(Block.STONE_PRESSURE_PLATE), "###", '#', Block.STONE);
            replaceCraftingRecipe(new ItemStack(Block.WOODEN_PRESSURE_PLATE), "###", '#', Block.PLANKS);
        }
        // < Beta 1.2
        if (v.compareTo(ProtocolVersion.BETA_8) < 0) {
            removeCraftingRecipes(new ItemStack(Item.CAKE), new ItemStack(Block.DISPENSER), new ItemStack(Block.NOTE_BLOCK),
                    new ItemStack(Block.SANDSTONE), new ItemStack(Item.SUGAR));
            for (int i = 0; i < 16; i++) {
                removeCraftingRecipes(new ItemStack(Block.WOOL, 1, ~i & 15), new ItemStack(Item.DYE, 1, i));
            }
            SmeltingRecipeManager.getInstance().getRecipes().remove(Block.CACTUS.id);
            SmeltingRecipeManager.getInstance().getRecipes().remove(Block.LOG.id);
        }
        Multiproto.LOGGER.info("Applied version recipe parity");
    }

    public static void replaceCraftingRecipe(ItemStack output, Object... recipe) {
        replaceCraftingRecipe(output, output, recipe);
    }

    public static void replaceCraftingRecipe(ItemStack oldOutput, ItemStack output, Object... recipe) {
        removeCraftingRecipes(oldOutput);
        CraftingRegistry.addShapedRecipe(output, recipe);
    }

    public static void removeCraftingRecipes(ItemStack... outputs) {
        for (ItemStack output : outputs) {
            CraftingRecipeManager.getInstance().getRecipes().removeIf(r -> ((CraftingRecipe) r).getOutput().equals(output));
        }
    }
}
