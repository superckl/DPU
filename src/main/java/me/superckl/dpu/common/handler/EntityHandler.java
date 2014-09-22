package me.superckl.dpu.common.handler;

import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EntityHandler {

	@SubscribeEvent
	public void onEntityPickupItem(final EntityItemPickupEvent e){
		if(e.entityPlayer == null)
			return;
		final ItemStack item = e.item.getEntityItem();
		for(final ItemStack stack:e.entityPlayer.inventory.mainInventory)
			if(stack != null && stack.getItem() == ModItems.excludifier && stack.hasTagCompound()){
				final NBTTagCompound comp = stack.getTagCompound();
				final NBTTagList list = comp.getTagList("items", NBT.TAG_COMPOUND);
				for(int i = 0; i < list.tagCount(); i++)
					if(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)).isItemEqual(item)){
						e.setCanceled(true);
						LogHelper.info("denied pickup of "+item.getDisplayName());
						return;
					}
			}
	}

}
