package me.superckl.dpu.proxy;

import me.superckl.dpu.common.network.MessageConfigSync;
import me.superckl.dpu.common.network.MessageDeleteItem;
import me.superckl.dpu.common.network.MessageHandlerConfigSync;
import me.superckl.dpu.common.network.MessageHandlerDeleteItem;
import me.superckl.dpu.common.network.MessageHandlerNoSearch;
import me.superckl.dpu.common.network.MessageNoSearch;
import me.superckl.dpu.common.reference.ModData;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy{

	@Override
	public void registerHandlers() {
		super.registerHandlers();
		ModData.ITEM_DELETE_CHANNEL.registerMessage(MessageHandlerDeleteItem.class, MessageDeleteItem.class, 0, Side.CLIENT);
		ModData.CONFIG_SYNC_CHANNEL.registerMessage(MessageHandlerConfigSync.class, MessageConfigSync.class, 0, Side.CLIENT);
		ModData.GUI_UPDATE_CHANNEL.registerMessage(MessageHandlerNoSearch.class, MessageNoSearch.class, 0, Side.CLIENT);
	}



}
