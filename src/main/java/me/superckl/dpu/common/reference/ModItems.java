package me.superckl.dpu.common.reference;

import me.superckl.dpu.common.item.ItemExcludifier;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(ModData.MOD_ID)
public class ModItems {

	public static final ItemExcludifier excludifier = new ItemExcludifier();

	public static void init(){
		GameRegistry.registerItem(ModItems.excludifier, "excludifier");
	}

}
