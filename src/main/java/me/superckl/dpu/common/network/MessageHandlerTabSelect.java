package me.superckl.dpu.common.network;

import me.superckl.dpu.common.container.ContainerExcludify;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerTabSelect implements IMessageHandler<MessageTabSelect, IMessage>{

	@Override
	public IMessage onMessage(final MessageTabSelect message, final MessageContext ctx) {
		final ContainerExcludify cont = (ContainerExcludify) ctx.getServerHandler().playerEntity.openContainer;
		if(message.isOnlyActive())
			cont.addActiveSlots();
		else
			cont.addSearchSlots();
		return null;
	}

}
