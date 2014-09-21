package me.superckl.dpu.common.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.superckl.dpu.ItemHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerExcludify extends Container{

	private final EntityPlayer player;
	private final InventoryBasic inventory = new InventoryBasic("tmp", true, 72);
	public List<ItemStack> itemList;

	public ContainerExcludify(final EntityPlayer player){
		this.player = player;
		this.inventory.func_110133_a("Excludify");
		this.refreshItemList(false);
	}

	public void refreshItemList(final boolean onlyActive){
		this.itemList = new ArrayList<ItemStack>();
		final Iterator it = ItemHandler.getAllItems().iterator();
		Item item;
		while(it.hasNext())
			(item = (Item)it.next()).getSubItems(item, CreativeTabs.tabAllSearch, this.itemList);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer p_75145_1_) {
		return true;
	}

	public void scrollTo(final float p_148329_1_)
	{
		final int i = this.itemList.size() / 9 - 5 + 1;
		int j = (int)(p_148329_1_ * i + 0.5D);

		if (j < 0)
			j = 0;

		for (int k = 0; k < 5; ++k)
			for (int l = 0; l < 9; ++l)
			{
				final int i1 = l + (k + j) * 9;

				if (i1 >= 0 && i1 < this.itemList.size())
					this.inventory.setInventorySlotContents(l + k * 9, this.itemList.get(i1));
				else
					this.inventory.setInventorySlotContents(l + k * 9, (ItemStack)null);
			}
	}

}
