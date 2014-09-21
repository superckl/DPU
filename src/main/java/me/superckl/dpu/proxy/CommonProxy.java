package me.superckl.dpu.proxy;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.gui.GuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public abstract class CommonProxy implements IProxy{

	@Override
	public void registerHandlers(){
		NetworkRegistry.INSTANCE.registerGuiHandler(DPUMod.getInstance(), new GuiHandler());
	}

}
