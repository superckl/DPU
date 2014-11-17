package me.superckl.dpu.common.network;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

//TODO return no delete message
@ExtensionMethod(ItemStackHelper.class)
public class MessageHandlerDeleteItemUpdate implements IMessageHandler<MessageDeleteItemUpdate, IMessage>{

	@Override
	public IMessage onMessage(final MessageDeleteItemUpdate message,
			final MessageContext ctx) {
		final EntityPlayer player = ctx.getServerHandler().playerEntity;
		final ItemStack stack = player.getHeldItem();
		if(!stack.ensureExcludeNBT()){
			player.closeScreen();
			return null;
		}
		final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		list.getCompoundTagAt(message.getSelectedIndex()).setBoolean("dpuDelete", message.isDelete());
		return null;
	}

}
