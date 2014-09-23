package me.superckl.dpu.common.network;

import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerItemSelect implements IMessageHandler<MessageItemSelect, IMessage>{

	@Override
	public IMessage onMessage(final MessageItemSelect message, final MessageContext ctx) {
		LogHelper.info("handling message");

		final EntityPlayer player = ctx.getServerHandler().playerEntity;
		final ItemStack stack = player.getHeldItem();
		if(stack == null || stack.getItem() != ModItems.excludifier){
			player.closeScreen();
			return null;
		}
		if(!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("items", new NBTTagList());
		}
		final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		if(message.isAdded())
			list.appendTag(message.getStack().writeToNBT(new NBTTagCompound()));
		//((ContainerExcludify)player.openContainer).onActiveItemChange(message.getSelectedIndex(), true);
		else
			list.removeTag(message.getSelectedIndex());
		//((ContainerExcludify)player.openContainer).onActiveItemChange(message.getSelectedIndex(), false);
		LogHelper.info("finished handling");
		return null;
	}

}
