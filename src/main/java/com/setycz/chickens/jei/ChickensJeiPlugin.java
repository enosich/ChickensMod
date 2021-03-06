package com.setycz.chickens.jei;

import java.util.ArrayList;
import java.util.List;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.item.ItemSpawnEgg;
import com.setycz.chickens.jei.breeding.BreedingRecipeCategory;
import com.setycz.chickens.jei.breeding.BreedingRecipeHandler;
import com.setycz.chickens.jei.breeding.BreedingRecipeWrapper;
import com.setycz.chickens.jei.drop.DropRecipeCategory;
import com.setycz.chickens.jei.drop.DropRecipeHandler;
import com.setycz.chickens.jei.drop.DropRecipeWrapper;
import com.setycz.chickens.jei.henhousing.HenhousingRecipeCategory;
import com.setycz.chickens.jei.henhousing.HenhousingRecipeHandler;
import com.setycz.chickens.jei.henhousing.HenhousingRecipeWrapper;
import com.setycz.chickens.jei.laying.LayingRecipeCategory;
import com.setycz.chickens.jei.laying.LayingRecipeHandler;
import com.setycz.chickens.jei.laying.LayingRecipeWrapper;
import com.setycz.chickens.jei.throwing.ThrowingRecipeCategory;
import com.setycz.chickens.jei.throwing.ThrowingRecipeHandler;
import com.setycz.chickens.jei.throwing.ThrowingRecipeWrapper;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by setyc on 21.02.2016.
 */
@JEIPlugin
public class ChickensJeiPlugin implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        
        registry.addRecipeCategories(
                new LayingRecipeCategory(jeiHelpers.getGuiHelper()),
                new BreedingRecipeCategory(jeiHelpers.getGuiHelper()),
                new DropRecipeCategory(jeiHelpers.getGuiHelper()),
                new ThrowingRecipeCategory(jeiHelpers.getGuiHelper()),
                new HenhousingRecipeCategory(jeiHelpers.getGuiHelper())
        );
	}
	
    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    	subtypeRegistry.useNbtForSubtypes(ChickensMod.spawnEgg);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @SuppressWarnings("deprecation")
	@Override
    public void register(IModRegistry registry) {
        
    	registry.addRecipeHandlers(
                new LayingRecipeHandler(),
                new BreedingRecipeHandler(),
                new DropRecipeHandler(),
                new ThrowingRecipeHandler(),
                new HenhousingRecipeHandler()
        );
    	
        registry.addRecipes(getLayingRecipes());
        registry.addRecipes(getBreedingRecipes());
        registry.addRecipes(getDropRecipes());
        registry.addRecipes(getThrowRecipes());
        registry.addRecipes(getHenhouseRecipes());
    }
 
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }

    private List<LayingRecipeWrapper> getLayingRecipes() {
        List<LayingRecipeWrapper> result = new ArrayList<LayingRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {

            ItemStack itemstack = new ItemStack(ChickensMod.spawnEgg, 1);
            ItemSpawnEgg.applyEntityIdToItemStack(itemstack, chicken.getRegistryName()); 
            
            result.add(new LayingRecipeWrapper(
            		itemstack,
                    chicken.createLayItem(),
                    chicken.getMinLayTime(), chicken.getMaxLayTime()
            ));
        }
        return result;
    }

    private List<DropRecipeWrapper> getDropRecipes() {
        List<DropRecipeWrapper> result = new ArrayList<DropRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {

            ItemStack itemstack = new ItemStack(ChickensMod.spawnEgg, 1);
            ItemSpawnEgg.applyEntityIdToItemStack(itemstack, chicken.getRegistryName()); 
            
            result.add(new DropRecipeWrapper(
            		itemstack,
                    chicken.createDropItem()
            ));
        }
        return result;
    }

    private List<BreedingRecipeWrapper> getBreedingRecipes() {
        List<BreedingRecipeWrapper> result = new ArrayList<BreedingRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
            if (chicken.isBreedable()) {

                ItemStack itemstack = new ItemStack(ChickensMod.spawnEgg, 1);
                ItemSpawnEgg.applyEntityIdToItemStack(itemstack, chicken.getRegistryName()); 
                
                ItemStack parent1 = new ItemStack(ChickensMod.spawnEgg, 1);
                ItemSpawnEgg.applyEntityIdToItemStack(parent1, chicken.getParent1().getRegistryName()); 
                
                  
                ItemStack parent2 = new ItemStack(ChickensMod.spawnEgg, 1);
                ItemSpawnEgg.applyEntityIdToItemStack(parent2, chicken.getParent2().getRegistryName()); 

                //noinspection ConstantConditions
                result.add(new BreedingRecipeWrapper(
                		parent1,
                		parent2,
                        itemstack,
                        ChickensRegistry.getChildChance(chicken)
                ));
            }
        }
        return result;
    }

    private List<ThrowingRecipeWrapper> getThrowRecipes() {
        List<ThrowingRecipeWrapper> result = new ArrayList<ThrowingRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
            if (chicken.isDye()) {

                ItemStack itemstack = new ItemStack(ChickensMod.spawnEgg, 1);
                ItemSpawnEgg.applyEntityIdToItemStack(itemstack, chicken.getRegistryName()); 
                
                result.add(new ThrowingRecipeWrapper(
                        new ItemStack(ChickensMod.coloredEgg, 1, chicken.getDyeMetadata()),
                        itemstack));
            }
        }
        return result;
    }

    private List<HenhousingRecipeWrapper> getHenhouseRecipes() {
        List<HenhousingRecipeWrapper> henhouseRecipes = new ArrayList<HenhousingRecipeWrapper>();
        henhouseRecipes.add(new HenhousingRecipeWrapper(new ItemStack(Blocks.HAY_BLOCK), new ItemStack(Blocks.DIRT)));
        return henhouseRecipes;
    }

}
