package me.superckl.dpu.common.network;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

@ExtensionMethod(ItemStackHelper.class)
public class MessageHandlerDisableExcludifier implements IMessageHandler<MessageDisableExcludifier, MessageDisableExcludifier>{

	@Override
	public MessageDisableExcludifier onMessage(final MessageDisableExcludifier message, final MessageContext ctx) {
		final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
		if(player != null && player.getHeldItem() != null){
			final ItemStack stack = player.getHeldItem();
			if(!stack.ensureExcludeNBT())
				return null;
			final NBTTagCompound comp = stack.getTagCompound();
			comp.setBoolean("disabled", !comp.getBoolean("disabled"));
		}
		return null;
	}

}
