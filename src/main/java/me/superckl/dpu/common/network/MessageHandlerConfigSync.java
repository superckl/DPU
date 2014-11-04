package me.superckl.dpu.common.network;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.utlilty.LogHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerConfigSync implements IMessageHandler<MessageConfigSync, IMessage>{

	@Override
	public IMessage onMessage(final MessageConfigSync message, final MessageContext ctx) {
		LogHelper.info("Received config sync packet from server...");
		DPUMod.getInstance().getConfig().deserialize(message.getTag());
		return null;
	}

}
