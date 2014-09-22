package me.superckl.dpu.common.gui;

import java.util.Set;

import me.superckl.dpu.client.gui.GuiConfigDPU;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory{

	@Override
	public void initialize(final Minecraft minecraftInstance) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return GuiConfigDPU.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			final RuntimeOptionCategoryElement element) {
		return null;
	}

}
