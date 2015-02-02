package me.superckl.dpu.common.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

@ExtensionMethod(ItemStackHelper.class)
public class RecipeExcludifierNBT implements IRecipe{

	@Override
	public boolean matches(final InventoryCrafting crafting, final World world) {
		return this.matches(crafting);
	}

	private boolean matches(final IInventory crafting){
		if(!DPUMod.getInstance().getConfig().isAllowNBTCopying())
			return false;
		final ItemStack req = crafting.getStackInSlot(0);
		if(req == null || req.getItem() != ModItems.excludifier)
			return false;
		final List<Integer> matches = new ArrayList<Integer>();
		for(int i = 1; i < crafting.getSizeInventory(); i++){
			final ItemStack stack = crafting.getStackInSlot(i);
			if(stack == null)
				continue;
			else if(stack.getItem() != ModItems.excludifier)
				return false;
			matches.add(i);
		}
		return !matches.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting crafting) {
		final List<ItemStack> exc = new ArrayList<ItemStack>();
		for(int i = 1; i < crafting.getSizeInventory(); i++){
			final ItemStack stack = crafting.getStackInSlot(i);
			if(stack != null && stack.getItem() == ModItems.excludifier)
				exc.add(stack);
		}
		List<ItemStack> master = new ArrayList<ItemStack>();
		for(final ItemStack stack:exc){
			if(!stack.ensureExcludeNBT())
				continue;
			final NBTTagList list = stack.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
			master.addAll(list.convert());
		}
		master = master.removeDuplicates();
		final ItemStack main = crafting.getStackInSlot(0).copy();
		if(!main.ensureExcludeNBT())
			return null;
		final NBTTagList list = main.getTagCompound().getTagList("items", NBT.TAG_COMPOUND);
		final List<ItemStack> mainItems = list.convert();
		final Iterator<ItemStack> it = master.iterator();
		while(it.hasNext())
			if(mainItems.find(it.next()) != -1)
				it.remove();
		mainItems.addAll(master);
		final NBTTagList newList = new NBTTagList();
		for(final ItemStack stack:mainItems)
			newList.appendTag(stack.writeToNBT(new NBTTagCompound()));
		main.getTagCompound().setTag("items", newList);
		return main;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(ModItems.excludifier);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onPlayerCrafting(final ItemCraftedEvent e){
		if(this.matches(e.craftMatrix) && e.crafting.getItem() == ModItems.excludifier)
			e.craftMatrix.setInventorySlotContents(0, null);
		else if(e.crafting.getItem() == ModItems.excludifier){
			final List<Integer> exc = new ArrayList<Integer>();
			for(int i = 0; i < e.craftMatrix.getSizeInventory(); i++){
				final ItemStack stack = e.craftMatrix.getStackInSlot(i);
				if(stack == null)
					continue;
				else if(stack.getItem() != ModItems.excludifier)
					return;
				exc.add(i);
			}
			if(exc.size() != 1)
				return;
			e.craftMatrix.setInventorySlotContents(exc.get(0), null);
		}
	}

}
