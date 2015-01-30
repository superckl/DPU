package me.superckl.dpu.client.gui;

import java.util.ArrayList;
import java.util.List;

import me.superckl.dpu.Config.Category;
import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.utlilty.StringHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GuiConfigDPU extends GuiConfig{

	public GuiConfigDPU(final GuiScreen parentScreen) {
		super(parentScreen, GuiConfigDPU.getConfigElements(), ModData.MOD_ID, false,
				false, LanguageRegistry.instance().getStringLocalization(StringHelper.formatGUIUnlocalizedName("config")));
	}

	private static List<IConfigElement> getConfigElements(){
		final List<IConfigElement> list = new ArrayList<IConfigElement>();
		list.add(new DummyConfigElement.DummyCategoryElement("General", "dpu.configgui.ctgy.general", GeneralCategory.class));
		if(Loader.isModLoaded("NotEnoughItems"))
			list.add(new DummyConfigElement.DummyCategoryElement("NEI", "dpu.configgui.ctgy.nei", NEICategory.class));
		return list;
	}

	public static class GeneralCategory extends CategoryEntry{

		public GeneralCategory(final GuiConfig owningScreen,
				final GuiConfigEntries owningEntryList, final IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
		}

		@Override
		protected GuiScreen buildChildScreen(){
			return new GuiConfig(this.owningScreen, new ConfigElement(DPUMod.getInstance().getConfig().getConfigFile().getCategory(Category.GENERAL)).getChildElements()
					, ModData.MOD_ID, false, false, LanguageRegistry.instance().getStringLocalization(StringHelper.formatGUIUnlocalizedName("config_general")));
		}

	}

	public static class NEICategory extends CategoryEntry{

		public NEICategory(final GuiConfig owningScreen,
				final GuiConfigEntries owningEntryList, final IConfigElement configElement) {
			super(owningScreen, owningEntryList, configElement);
		}

		@Override
		protected GuiScreen buildChildScreen(){
			return new GuiConfig(this.owningScreen, new ConfigElement(DPUMod.getInstance().getConfig().getConfigFile().getCategory(Category.NEI)).getChildElements()
					, ModData.MOD_ID, false, false, LanguageRegistry.instance().getStringLocalization(StringHelper.formatGUIUnlocalizedName("config_nei")));
		}

	}

}
