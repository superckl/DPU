package me.superckl.dpu.common.network;

import java.util.Random;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MessageHandlerDeleteItem implements IMessageHandler<MessageDeleteItem, IMessage>{

	@SideOnly(Side.CLIENT)
	private final Minecraft mc;
	private final Random random = new Random();

	@SideOnly(Side.CLIENT)
	public MessageHandlerDeleteItem() {
		this.mc = Minecraft.getMinecraft();
	}

	@Override
	public IMessage onMessage(final MessageDeleteItem message, final MessageContext ctx) {
		for(int i = 0; i < this.random.nextInt(3)+1;i++)
			this.mc.theWorld.spawnParticle("flame", message.getX()+(this.random.nextFloat()-1)/8F, message.getY()+this.random.nextFloat()/3F, message.getZ()+(this.random.nextFloat()-1)/8F, 0D, 0D, 0D);
		return null;
	}

}
