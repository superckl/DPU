package me.superckl.dpu.common.container;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import me.superckl.dpu.common.utlilty.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

public class SlotSearch extends Slot{

	@Getter
	@Setter
	private boolean selected;
	@Getter
	@Setter
	private int selectedIndex;

	public SlotSearch(final IInventory inventory, final int id, final int x, final int y) {
		super(inventory, id, x, y);
	}
	public SlotSearch(final IInventory inventory, final int id, final int x, final int y, final boolean selected, final int index) {
		this(inventory, id, x, y);
		this.selected = selected;
		this.selectedIndex = index;
	}

	public ItemStack onClick(final EntityPlayer player, final ItemStack held){
		final ItemStack stack = player.getHeldItem();
		if(stack == null || stack.getItem() != ModItems.excludifier){
			player.closeScreen();
			return null;
		}
		if(!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("items", new NBTTagList());
		}
		if(!this.getHasStack())
			return null;
		final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		if(this.selected){
			list.removeTag(this.selectedIndex);
			this.selected = false;
			stack.getTagCompound().setTag("items", list);
			((ContainerExcludify)player.openContainer).onActiveItemChange(this.selectedIndex, false);
			this.selectedIndex = 0;
			LogHelper.info("removed");
		}else{
			this.selected = true;
			this.selectedIndex = list.tagCount();
			final List<ItemStack> items = ItemStackHelper.convert(list);
			if(ItemStackHelper.contains(items, this.getStack()) != -1)
				return null;
			list.appendTag(this.getStack().writeToNBT(new NBTTagCompound()));
			stack.getTagCompound().setTag("items", list);
			((ContainerExcludify)player.openContainer).onActiveItemChange(this.selectedIndex, true);
			LogHelper.info("added");
		}
		//super.onSlotChanged();
		return null;
	}

	@Override
	public boolean isItemValid(final ItemStack p_75214_1_) {
		return false;
	}

	/*@Override
	public boolean canTakeStack(EntityPlayer p_82869_1_) {
		return false;
	}*/

}
