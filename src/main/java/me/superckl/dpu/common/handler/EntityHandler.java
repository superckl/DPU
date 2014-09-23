package me.superckl.dpu.common.handler;

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
				for(int i = 0; i < list.tagCount(); i++)
					if(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)).isItemEqual(item)){
						e.setCanceled(true);
						if(DPUMod.getInstance().getConfig().isJustDelete()){
							e.item.setDead();
							//LogHelper.info(FMLCommonHandler.instance().getEffectiveSide()+": spawning flame at "+e.item.posX+":"+e.item.posY+":"+e.item.posZ);
							ModData.ITEM_DELETE_CHANNEL.sendToAllAround(new MessageDeleteItem(e.item.posX, e.item.posY, e.item.posZ), new TargetPoint(e.entityPlayer.dimension, e.item.posX, e.item.posY, e.item.posZ, 20D));
							//e.entityPlayer.worldObj.spawnParticle("flame", e.item.posX, e.item.posY, e.item.posZ, 0D, 0D, 0D);
						}
						//LogHelper.info("denied pickup of "+item.getDisplayName());
						return;
					}
			}
	}

}
