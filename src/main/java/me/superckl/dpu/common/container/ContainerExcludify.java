package me.superckl.dpu.common.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.superckl.dpu.ItemHandler;
import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerExcludify extends Container{

	private final EntityPlayer player;
	private InventoryBasic inventoryActive;
	@SideOnly(Side.CLIENT)
	private InventoryBasic inventoryCreative;
	private InventoryBasic activeInventory;
	private float currentScroll;
	public List<ItemStack> itemList;

	public ContainerExcludify(final EntityPlayer player){
		this.player = player;
		this.refreshActiveList();
		this.addActiveSlots();
	}

	@SideOnly(Side.CLIENT)
	public void refreshCreativeList(){
		this.itemList = new ArrayList<ItemStack>();
		final Iterator it = ItemHandler.getAllItems().iterator();
		Item item;
		while(it.hasNext())
			(item = (Item)it.next()).getSubItems(item, CreativeTabs.tabAllSearch, this.itemList);
	}

	public void refreshActiveList(){
		this.itemList = new ArrayList<ItemStack>();
		final ItemStack stack = this.player.getHeldItem();
		if(stack == null || stack.getItem() != ModItems.excludifier){
			this.player.closeScreen();
			return;
		}
		if(!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("items", new NBTTagList());
			return;
		}
		final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
			this.itemList.add(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)));
	}

	@SideOnly(Side.CLIENT)
	public void addSearchSlots(){
		if(this.inventoryCreative == null)
			this.inventoryCreative = new InventoryBasic("tmpCreative", false, 72);
		this.clearInventory(this.inventoryCreative);
		this.activeInventory = this.inventoryCreative;
		this.inventorySlots.clear();
		this.refreshCreativeList();
		//Add all search slots
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new SlotSearch(this.inventoryCreative, i*9+j, j*18+9, i*18+18));
		this.scrollTo(0F);
	}

	private void bindPlayerInventoryAt(final int x, final int y) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(this.player.inventory, j + i * 9 + 9,
						x + j * 18, y + i * 18));

		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(this.player.inventory, i, x + i * 18, y+58));
	}

	public void addActiveSlots(){
		if(this.inventoryActive == null)
			this.inventoryActive = new InventoryBasic("tmpActive", false, 27);
		this.clearInventory(this.activeInventory);
		this.activeInventory = this.inventoryActive;
		this.inventorySlots.clear();
		this.refreshActiveList();
		//Add all display slots
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new SlotDisplay(this.inventoryActive, i*9+j, j*18+9, i*18+18));
		this.bindPlayerInventoryAt(9, 86);
		this.scrollTo(0F);
	}

	public void clearInventory(final IInventory inventory){
		if(inventory == null)
			return;
		for(int i = 0; i < inventory.getSizeInventory(); i++)
			inventory.setInventorySlotContents(i, null);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer player) {
		return player == this.player;
	}

	@Override
	public void putStackInSlot(final int p_75141_1_, final ItemStack p_75141_2_) {}

	public void scrollTo(final float scroll)
	{
		if(this.activeInventory == null)
			return;
		final ItemStack stack = this.player.getHeldItem();
		if(stack == null || stack.getItem() != ModItems.excludifier){
			this.player.closeScreen();
			return;
		}
		if(!stack.hasTagCompound()){
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setTag("items", new NBTTagList());
			return;
		}
		final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		final List<ItemStack> items = ItemStackHelper.convert(list);
		final int i = this.itemList.size() / 9 - (this.activeInventory == this.inventoryActive ? 3:8) + 1;
		int j = (int)(scroll * i + 0.5D);

		if (j < 0)
			j = 0;

		for (int k = 0; k < (this.activeInventory == this.inventoryActive ? 3:8); ++k)
			for (int l = 0; l < 9; ++l)
			{
				final int i1 = l + (k + j) * 9;

				if (i1 >= 0 && i1 < this.itemList.size()){
					final ItemStack stack1 = this.itemList.get(i1);
					this.activeInventory.setInventorySlotContents(l + k * 9, stack1);
					Slot slot;
					int index;
					if((index = ItemStackHelper.contains(items, stack1)) != -1 && (slot = this.getSlot(l + k * 9)) instanceof SlotSearch){
						((SlotSearch)slot).setSelected(true);
						((SlotSearch)slot).setSelectedIndex(index);
						((SlotSearch)slot).setDelete(list.getCompoundTagAt(index).getBoolean("dpuDelete"));
						//stack.getTagCompound().setTag("items", list);
					}else if((slot = this.getSlot(l + k * 9)) instanceof SlotSearch)
						((SlotSearch)slot).setSelected(false);
				} else
					this.activeInventory.setInventorySlotContents(l + k * 9, (ItemStack)null);
			}
	}

	public void onActiveItemChange(final int index, final boolean added){
		if(this.activeInventory != this.inventoryActive && !added){
			for(final Object obj:this.inventorySlots){
				if(obj instanceof SlotSearch == false)
					continue;
				final SlotSearch slot = (SlotSearch) obj;
				if(slot.isSelected() && slot.getSelectedIndex() > index)
					slot.setSelectedIndex(slot.getSelectedIndex()-1);
			}
			return;
		}else if(this.activeInventory == this.inventoryActive){
			this.refreshActiveList();
			this.scrollTo(this.currentScroll);
		}
	}

	@Override
	protected void retrySlotClick(final int p_75133_1_, final int p_75133_2_, final boolean p_75133_3_, final EntityPlayer p_75133_4_) {}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer p_82846_1_, final int p_82846_2_) {
		// TODO
		return null;
	}

}
