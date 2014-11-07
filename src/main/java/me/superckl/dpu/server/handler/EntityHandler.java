package me.superckl.dpu.server.handler;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.network.MessageDeleteItem;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.reference.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

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
				for(int i = 0; i < list.tagCount(); i++){
					final ItemStack dpuStack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
					if(dpuStack.isItemEqual(item)){
						e.setCanceled(true);
						if(DPUMod.getInstance().getConfig().isAllowDelete() && list.getCompoundTagAt(i).getBoolean("dpuDelete")){
							e.item.setDead();
							ModData.ITEM_DELETE_CHANNEL.sendToAllAround(new MessageDeleteItem(e.item.posX, e.item.posY, e.item.posZ), new TargetPoint(e.entityPlayer.dimension, e.item.posX, e.item.posY, e.item.posZ, 20D));
						}
						return;
					}
				}
			}
	}

}
