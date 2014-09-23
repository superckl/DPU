package me.superckl.dpu.common.network;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.client.gui.GuiContainerExcludify;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MessageHandlerNoSearch implements IMessageHandler<MessageNoSearch, IMessage>{

	@SideOnly(Side.CLIENT)
	private final Minecraft mc;

	@SideOnly(Side.CLIENT)
	public MessageHandlerNoSearch() {
		this.mc = Minecraft.getMinecraft();
	}

	@Override
	public IMessage onMessage(final MessageNoSearch message, final MessageContext ctx) {
		DPUMod.getInstance().getConfig().setNoSearchOverride();
		if(this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiContainerExcludify)
			((GuiContainerExcludify)this.mc.currentScreen).onNoSearchReceived(message);
		return null;
	}

}
