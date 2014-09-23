package me.superckl.dpu.common.container;

import java.util.List;

import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import me.superckl.dpu.common.utlilty.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class SlotDisplay extends SlotSearch{

	public SlotDisplay(final IInventory inventory, final int id, final int x, final int y) {
		super(inventory, id, x, y, true, id);
	}

	@Override
	public ItemStack onClick(final EntityPlayer player, ItemStack held) {
		final ItemStack stack = player.getHeldItem();
		if(stack == null || stack.getItem() != ModItems.excludifier){
			player.closeScreen();
			return null;
		}
		if(!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("items", new NBTTagList());
		}
		if(!this.getHasStack() && held != null){
			held = held.copy();
			held.stackSize = 1;
			final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
			final List<ItemStack> items = ItemStackHelper.convert(list);
			if(ItemStackHelper.contains(items, held) != -1)
				return null;
			this.setSelectedIndex(list.tagCount());
			list.appendTag(held.writeToNBT(new NBTTagCompound()));
			stack.getTagCompound().setTag("items", list);
			((ContainerExcludify)player.openContainer).onActiveItemChange(this.getSelectedIndex(), true);
			LogHelper.info("added");
			return null;
		}else
			return super.onClick(player, held);
	}



}
