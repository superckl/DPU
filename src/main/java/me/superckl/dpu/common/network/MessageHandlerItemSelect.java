package me.superckl.dpu.common.network;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.reference.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageHandlerItemSelect implements IMessageHandler<MessageItemSelect, MessageNoSearch>{

	@Override
	public MessageNoSearch onMessage(final MessageItemSelect message, final MessageContext ctx) {
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
		if(message.isFromSearch() && DPUMod.getInstance().getConfig().isNoCreativeSearch())
			return new MessageNoSearch(message.getSelectedIndex());
		final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		if(message.isAdded()){
			list.appendTag(message.getStack().writeToNBT(new NBTTagCompound()));
			if(message.getStack().hasTagCompound() && message.getStack().getTagCompound().getBoolean("dpuDelete"))
				list.getCompoundTagAt(list.tagCount()-1).setBoolean("dpuDelete", true);
		}else
			list.removeTag(message.getSelectedIndex());
		return null;
	}

}
