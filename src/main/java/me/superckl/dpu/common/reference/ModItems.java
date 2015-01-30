package me.superckl.dpu.common.reference;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.crafting.RecipeExcludifierNBT;
import me.superckl.dpu.common.item.ItemExcludifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModData.MOD_ID)
public class ModItems {

	public static final ItemExcludifier excludifier = new ItemExcludifier();

	public static void init(){
		GameRegistry.registerItem(ModItems.excludifier, "excludifier");
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.excludifier), ModItems.excludifier);
		GameRegistry.addRecipe(new RecipeExcludifierNBT());
		OreDictionary.registerOre("glass", Blocks.glass);
		if(DPUMod.getInstance().getConfig().isEasyRecipe())
			GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.excludifier, "xyx", "xzx", "xwx", 'x', "stone", 'y', "glass", 'z', Blocks.stone_button, 'w', Blocks.stone_slab));
		else
			GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.excludifier, "xyx", "xzx", "xwx", 'x', "ingotIron", 'y', "glass", 'z', Items.ender_pearl, 'w', Items.redstone));
		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ModItems.excludifier), 1, 1, 1));
	}

}
