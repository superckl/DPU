package me.superckl.dpu.common.network;

import me.superckl.dpu.common.container.ContainerExcludify;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerScrollUpdate implements IMessageHandler<MessageScrollUpdate, IMessage>{

	@Override
	public IMessage onMessage(final MessageScrollUpdate message, final MessageContext ctx) {
		((ContainerExcludify)ctx.getServerHandler().playerEntity.openContainer).scrollTo(message.getScroll());
		return null;
	}

}
