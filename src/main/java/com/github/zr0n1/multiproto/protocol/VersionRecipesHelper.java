package com.github.zr0n1.multiproto.protocol;

import com.github.zr0n1.multiproto.Multiproto;
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

public class VersionRecipesHelper {

    public static List vanillaCraftingRecipes = new ArrayList();
    public static Map vanillaSmeltingRecipes = new HashMap();

    /**
     * Handles recipe changes between versions.
     */
    public static void applyChanges() {
        ProtocolVersion v = Multiproto.getVersion();
        CraftingRecipeManager.getInstance().getRecipes().clear();
        CraftingRecipeManager.getInstance().getRecipes().addAll(vanillaCraftingRecipes);
        SmeltingRecipeManager.getInstance().getRecipes().clear();
        SmeltingRecipeManager.getInstance().getRecipes().putAll(vanillaSmeltingRecipes);
        // Beta 1.7
        if(v.compareTo(ProtocolVersion.BETA_14) < 0) {
            CraftingRecipeManager.getInstance().getRecipes().removeIf(r ->
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.PISTON)) ||
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.STICKY_PISTON)) ||
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.SHEARS)));
        }
        // Beta 1.6
        if(v.compareTo(ProtocolVersion.BETA_13) < 0) {
            CraftingRecipeManager.getInstance().getRecipes().removeIf(r ->
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.TRAPDOOR, 2)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.MAP, 1)));
        }
        // Beta 1.5
        if(v.compareTo(ProtocolVersion.BETA_11) < 0) {
            CraftingRecipeManager.getInstance().getRecipes().removeIf(r ->
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.POWERED_RAIL, 6)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.DETECTOR_RAIL, 6)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.LADDER, 2)));
            CraftingRegistry.addShapedRecipe(new ItemStack(Block.LADDER, 1), "# #", "###", "# #", '#', Item.STICK);
        }
        // Beta 1.4
        if(v.compareTo(ProtocolVersion.BETA_10) < 0) {
            CraftingRecipeManager.getInstance().getRecipes().removeIf(r ->
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.COOKIE, 8)));
        }
        // Beta 1.3
        if(v.compareTo(ProtocolVersion.BETA_9) < 0) {
            CraftingRecipeManager.getInstance().getRecipes().removeIf(r ->
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.BED)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.REPEATER)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.SLAB, 3, 3)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.SLAB, 3, 2)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.SLAB, 3, 1)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.SLAB, 3)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.STONE_PRESSURE_PLATE, 1)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.WOODEN_PRESSURE_PLATE, 1)));
            CraftingRegistry.addShapedRecipe(new ItemStack(Block.SLAB, 3), "##", '#', Block.STONE);
            CraftingRegistry.addShapedRecipe(new ItemStack(Block.STONE_PRESSURE_PLATE, 1), "###", '#', Block.STONE);
            CraftingRegistry.addShapedRecipe(new ItemStack(Block.WOODEN_PRESSURE_PLATE, 1), "###", '#', Block.PLANKS);
        }
        // Beta 1.2
        if(v.compareTo(ProtocolVersion.BETA_8) < 0) {
            CraftingRecipeManager.getInstance().getRecipes().removeIf(r ->
                    ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.CAKE, 1)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.DISPENSER)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.NOTE_BLOCK)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.SANDSTONE)) ||
                            ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.SUGAR)));
            for(int i = 0; i < 16; i++) {
                int i1 = i;
                CraftingRecipeManager.getInstance().getRecipes().removeIf(r ->
                        ((CraftingRecipe)r).getOutput().equals(new ItemStack(Block.WOOL, 1, ~i1 & 15)) ||
                                ((CraftingRecipe)r).getOutput().equals(new ItemStack(Item.DYE,1 , i1)));
            }
            SmeltingRecipeManager.getInstance().getRecipes().remove(Block.CACTUS.id);
            SmeltingRecipeManager.getInstance().getRecipes().remove(Block.LOG.id);
        }
    }
}
