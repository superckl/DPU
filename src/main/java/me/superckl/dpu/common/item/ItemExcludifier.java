package me.superckl.dpu.common.item;

import java.util.List;

import lombok.experimental.ExtensionMethod;
import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.reference.ModTabs;
import me.superckl.dpu.common.utlilty.ItemStackHelper;
import me.superckl.dpu.common.utlilty.StringHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import org.lwjgl.input.Keyboard;

@ExtensionMethod(ItemStackHelper.class)
public class ItemExcludifier extends ItemDPU{

	public ItemExcludifier() {
		this.setMaxStackSize(1).setUnlocalizedName("excludifier").setCreativeTab(ModTabs.TAB_DPU).setContainerItem(this);
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean par4) {
		list.add("R-click to exclude items from pickup");
		if(!stack.ensureExcludeNBT())
			return;
		final NBTTagCompound compound = stack.getTagCompound();
		if(compound.getBoolean("disabled"))
			list.add(StringHelper.build(EnumChatFormatting.RED, "Disabled"));
		final NBTTagList nbtList = compound.getTagList("items", NBT.TAG_COMPOUND);
		if(nbtList.tagCount() <= 0)
			return;
		list.add("");
		list.add("Currently excluding:");
		for(int i = 0; i < nbtList.tagCount() && i < 3; i++)
			list.add(ItemStack.loadItemStackFromNBT(nbtList.getCompoundTagAt(i)).getDisplayName());
		if(nbtList.tagCount() == 4){
			list.add(ItemStack.loadItemStackFromNBT(nbtList.getCompoundTagAt(3)).getDisplayName());
			return;
		}
		if(nbtList.tagCount() > 3)
			if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				list.add(StringHelper.build("Hold ", EnumChatFormatting.ITALIC, "Shift ",EnumChatFormatting.RESET, EnumChatFormatting.GRAY, "for more..."));
			else
				for(int i = 3; i < nbtList.tagCount(); i++)
					list.add(ItemStack.loadItemStackFromNBT(nbtList.getCompoundTagAt(i)).getDisplayName());
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(final ItemStack stack) {
		return false;
	}

	@Override
	public ItemStack getContainerItem(final ItemStack itemStack) {
		return itemStack.copy();
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
		if(!player.isSneaking())
			player.openGui(DPUMod.getInstance(), 0, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		return stack;
	}

	@Override
	public void registerIcons(final IIconRegister register) {
		this.itemIcon = register.registerIcon(ModData.MOD_ID+":excludifier");
	}

}
