package me.superckl.dpu.common.network;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

@ExtensionMethod(ItemStackHelper.class)
public class MessageHandlerItemSelect implements IMessageHandler<MessageItemSelect, MessageNoSearch>{

	@Override
	public MessageNoSearch onMessage(final MessageItemSelect message, final MessageContext ctx) {
		final EntityPlayer player = ctx.getServerHandler().playerEntity;
		final ItemStack stack = player.getHeldItem();
		if(!stack.ensureExcludeNBT()){
			player.closeScreen();
			return null;
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
