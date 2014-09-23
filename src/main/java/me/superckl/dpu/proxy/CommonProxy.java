package me.superckl.dpu.proxy;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.gui.GuiHandler;
import me.superckl.dpu.common.handler.EntityHandler;
import me.superckl.dpu.common.network.MessageDeleteItem;
import me.superckl.dpu.common.network.MessageHandlerDeleteItem;
import me.superckl.dpu.common.network.MessageHandlerItemSelect;
import me.superckl.dpu.common.network.MessageHandlerNoSearch;
import me.superckl.dpu.common.network.MessageItemSelect;
import me.superckl.dpu.common.network.MessageNoSearch;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.server.handler.ConnectionHandler;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public abstract class CommonProxy implements IProxy{

	@Override
	public void registerHandlers(){
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
		FMLCommonHandler.instance().bus().register(DPUMod.getInstance().getConfig());
		FMLCommonHandler.instance().bus().register(new ConnectionHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(DPUMod.getInstance(), new GuiHandler());
		ModData.GUI_UPDATE_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MOD_ID+":guiUpdate");
		ModData.GUI_UPDATE_CHANNEL.registerMessage(MessageHandlerItemSelect.class, MessageItemSelect.class, 0, Side.SERVER);
		ModData.GUI_UPDATE_CHANNEL.registerMessage(MessageHandlerNoSearch.class, MessageNoSearch.class, 1, Side.CLIENT);
		ModData.ITEM_DELETE_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MOD_ID+":itemDelete");
		ModData.ITEM_DELETE_CHANNEL.registerMessage(MessageHandlerDeleteItem.class, MessageDeleteItem.class, 0, Side.CLIENT);
	}

}
