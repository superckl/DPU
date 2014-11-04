package me.superckl.dpu.common.reference;

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
		OreDictionary.registerOre("glass", Blocks.glass);
		GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.excludifier,  "xyx", "xzx", "xwx", 'x', "ingotIron", 'y', "glass", 'z', Items.ender_pearl, 'w', Items.redstone));
		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(ModItems.excludifier), 1, 1, 1));
	}

}
