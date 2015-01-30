package me.superckl.dpu.common.container;

import java.util.List;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.common.network.MessageItemSelect;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@ExtensionMethod(ItemStackHelper.class)
public class SlotDisplay extends SlotSearch{

	public SlotDisplay(final IInventory inventory, final int id, final int x, final int y) {
		super(inventory, id, x, y, true, id);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean onClick(final EntityPlayer player, ItemStack held, final boolean fromSearch, final boolean leftClick) {
		final ItemStack stack = player.getHeldItem();
		if(!stack.ensureExcludeNBT()){
			player.closeScreen();
			return false;
		}
		if(held != null){
			held = held.copy();
			held.stackSize = 1;
			final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
			final List<ItemStack> items = ItemStackHelper.convert(list);
			if(ItemStackHelper.find(items, held) != -1)
				return false;
			this.setSelectedIndex(list.tagCount());
			list.appendTag(held.writeToNBT(new NBTTagCompound()));
			((ContainerExcludify)player.openContainer).onActiveItemChange(this.getSelectedIndex(), true);
			ModData.GUI_UPDATE_CHANNEL.sendToServer(new MessageItemSelect(held, this.getSelectedIndex(), true, fromSearch));
			return true;
		}else
			return super.onClick(player, held, fromSearch, leftClick);
	}



}
