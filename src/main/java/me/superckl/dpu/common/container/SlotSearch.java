package me.superckl.dpu.common.container;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.superckl.dpu.common.network.MessageItemSelect;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

	@SideOnly(Side.CLIENT)
	public boolean onClick(final EntityPlayer player, final ItemStack held, final boolean fromSearch){
		final ItemStack stack = player.getHeldItem();
		if(stack == null || stack.getItem() != ModItems.excludifier){
			player.closeScreen();
			return false;
		}
		if(!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("items", new NBTTagList());
		}
		if(!this.getHasStack())
			return false;
		final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		if(this.selected){
			list.removeTag(this.selectedIndex);
			this.selected = false;
			((ContainerExcludify)player.openContainer).onActiveItemChange(this.selectedIndex, false);
			ModData.GUI_UPDATE_CHANNEL.sendToServer(new MessageItemSelect(this.getStack(), this.selectedIndex, false, fromSearch));
			this.selectedIndex = 0;
		}else{
			this.selected = true;
			this.selectedIndex = list.tagCount();
			final List<ItemStack> items = ItemStackHelper.convert(list);
			if(ItemStackHelper.contains(items, this.getStack()) != -1)
				return false;
			list.appendTag(this.getStack().writeToNBT(new NBTTagCompound()));
			((ContainerExcludify)player.openContainer).onActiveItemChange(this.selectedIndex, true);
			ModData.GUI_UPDATE_CHANNEL.sendToServer(new MessageItemSelect(this.getStack(), this.selectedIndex, true, fromSearch));
		}
		return false;
	}

	@Override
	public boolean isItemValid(final ItemStack p_75214_1_) {
		return false;
	}

}
