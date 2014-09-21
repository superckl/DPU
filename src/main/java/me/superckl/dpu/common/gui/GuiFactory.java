package me.superckl.dpu.common.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory{

	@Override
	public void initialize(final Minecraft minecraftInstance) {
		// TODO Auto-generated method stub

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {

		return null;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			final RuntimeOptionCategoryElement element) {
		// TODO Auto-generated method stub
		return null;
	}

}
