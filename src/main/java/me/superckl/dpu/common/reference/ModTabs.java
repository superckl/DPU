package me.superckl.dpu.common.reference;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModTabs {

	public static final CreativeTabs TAB_DPU = new CreativeTabs(ModData.MOD_ID) {

		@Override
		public Item getTabIconItem() {
			return ModItems.excludifier;
		}

		@Override
		public String getTranslatedTabLabel() {
			return "Don't Pick Up";
		}

	};

}
