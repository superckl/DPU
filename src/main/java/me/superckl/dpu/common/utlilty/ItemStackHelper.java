package me.superckl.dpu.common.utlilty;

import java.util.ArrayList;
import java.util.List;

import me.superckl.dpu.common.reference.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemStackHelper {

	public static int contains(final List<ItemStack> stacks, final ItemStack stack){
		for(int i = 0; i < stacks.size(); i++){
			final ItemStack s = stacks.get(i);
			if(s == null)
				continue;
			if(s.isItemEqual(stack))
				return i;
		}
		return -1;
	}

	public static List<ItemStack> convert(final NBTTagList list){
		final List<ItemStack> items = new ArrayList<ItemStack>();
		for(int i = 0; i < list.tagCount(); i++)
			items.add(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)));
		return items;
	}

	public static boolean ensureExcludeNBT(final ItemStack stack){
		try {
			if(stack == null || stack.getItem() != ModItems.excludifier)
				return false;
			if(!stack.hasTagCompound()){
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setTag("items", new NBTTagList());
			}
		}catch (final Exception e) {
			LogHelper.info("Failed to check item NBT!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
