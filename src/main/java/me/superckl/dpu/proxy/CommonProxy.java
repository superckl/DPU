package me.superckl.dpu.proxy;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.gui.GuiHandler;
import me.superckl.dpu.common.handler.EntityHandler;
import me.superckl.dpu.common.network.MessageHandlerScrollUpdate;
import me.superckl.dpu.common.network.MessageHandlerTabSelect;
import me.superckl.dpu.common.network.MessageScrollUpdate;
import me.superckl.dpu.common.network.MessageTabSelect;
import me.superckl.dpu.common.reference.ModData;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public abstract class CommonProxy implements IProxy{

	@Override
	public void registerHandlers(){
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(DPUMod.getInstance(), new GuiHandler());
		ModData.TAB_SELECT_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MOD_ID+":tabSelect");
		ModData.TAB_SELECT_CHANNEL.registerMessage(new MessageHandlerTabSelect(), MessageTabSelect.class, 0, Side.SERVER);
		ModData.SCROLL_UPDATE_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MOD_ID+":scrollUpdate");
		ModData.SCROLL_UPDATE_CHANNEL.registerMessage(new MessageHandlerScrollUpdate(), MessageScrollUpdate.class, 0, Side.SERVER);
	}

}
