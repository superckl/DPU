package me.superckl.dpu.proxy;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.crafting.RecipeExcludifierNBT;
import me.superckl.dpu.common.gui.GuiHandler;
import me.superckl.dpu.common.handler.ConnectionHandler;
import me.superckl.dpu.common.network.MessageDeleteItemUpdate;
import me.superckl.dpu.common.network.MessageDisableExcludifier;
import me.superckl.dpu.common.network.MessageHandlerDeleteItemUpdate;
import me.superckl.dpu.common.network.MessageHandlerDisableExcludifier;
import me.superckl.dpu.common.network.MessageHandlerItemSelect;
import me.superckl.dpu.common.network.MessageItemSelect;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.server.handler.EntityHandler;
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
		FMLCommonHandler.instance().bus().register(DPUMod.getInstance().getVersionChecker());
		FMLCommonHandler.instance().bus().register(new RecipeExcludifierNBT());
		NetworkRegistry.INSTANCE.registerGuiHandler(DPUMod.getInstance(), new GuiHandler());
		ModData.GUI_UPDATE_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MOD_ID+":guiUpdate");
		ModData.GUI_UPDATE_CHANNEL.registerMessage(MessageHandlerItemSelect.class, MessageItemSelect.class, 1, Side.SERVER);
		ModData.GUI_UPDATE_CHANNEL.registerMessage(MessageHandlerDeleteItemUpdate.class, MessageDeleteItemUpdate.class, 2, Side.SERVER);
		ModData.ITEM_DELETE_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MOD_ID+":itemDelete");
		ModData.ITEM_DELETE_CHANNEL.registerMessage(MessageHandlerDisableExcludifier.class, MessageDisableExcludifier.class, 0, Side.SERVER);
		ModData.CONFIG_SYNC_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(ModData.MOD_ID+":configSync");
	}

}
