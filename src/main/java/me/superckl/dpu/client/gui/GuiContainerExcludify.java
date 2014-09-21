package me.superckl.dpu.client.gui;

import java.util.Iterator;

import me.superckl.dpu.ItemHandler;
import me.superckl.dpu.common.container.ContainerExcludify;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.utlilty.RenderHelper;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;

public class GuiContainerExcludify extends GuiContainer{

	private static final ResourceLocation textureSearch = new ResourceLocation(ModData.MOD_ID+":textures/gui/excludifysearch.png");
	private static final ResourceLocation textureActive = new ResourceLocation(ModData.MOD_ID+":textures/gui/excludifyactive.png");
	private static final ResourceLocation tabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

	private final EntityPlayer player;
	private GuiTextField textField;
	private float currentScroll;
	private boolean onlyActive;

	public GuiContainerExcludify(final EntityPlayer player) {
		super(new ContainerExcludify(player));
		this.player = player;
		this.xSize = 200;
		this.ySize = 200;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.textField = new GuiTextField(this.fontRendererObj, 10, 10, 60, 10);
	}

	@Override
	protected void keyTyped(final char p_73869_1_, final int p_73869_2_)
	{

		if (!this.checkHotbarKeys(p_73869_2_))
			if (this.textField.textboxKeyTyped(p_73869_1_, p_73869_2_))
				this.updateCreativeSearch();
			else
				super.keyTyped(p_73869_1_, p_73869_2_);
	}

	@Override
	protected void handleMouseClick(final Slot p_146984_1_, final int p_146984_2_, final int p_146984_3_, final int p_146984_4_)
	{

	}

	@Override
	protected void mouseMovedOrUp(final int mouseX, final int mouseY, final int action)
	{
		if (action == 0)
		{
			final int l = mouseX - this.guiLeft;
			final int i1 = mouseY - this.guiTop;
			//TODO determine click
		}

		super.mouseMovedOrUp(mouseX, mouseY, action);
	}

	private void updateCreativeSearch()
	{
		final ContainerExcludify containerExcludify = (ContainerExcludify) this.inventorySlots;
		containerExcludify.refreshItemList(false);
		this.updateFilteredItems(containerExcludify);
	}

	//split from above for custom search tabs
	private void updateFilteredItems(final ContainerExcludify containerExcludify)
	{
		Iterator iterator;
		final Enchantment[] aenchantment = Enchantment.enchantmentsList;
		final int j = aenchantment.length;

		for (int i = 0; i < j; ++i)
		{
			final Enchantment enchantment = aenchantment[i];

			if (enchantment != null && enchantment.type != null)
				Items.enchanted_book.func_92113_a(enchantment, containerExcludify.itemList);
		}

		iterator = containerExcludify.itemList.iterator();
		final String s1 = this.textField.getText().toLowerCase();

		while (iterator.hasNext())
		{
			final ItemStack itemstack = (ItemStack)iterator.next();
			boolean flag = false;
			final Iterator iterator1 = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips).iterator();

			while (true)
			{
				if (iterator1.hasNext())
				{
					final String s = (String)iterator1.next();

					if (!s.toLowerCase().contains(s1))
						continue;

					flag = true;
				}

				if (!flag)
					iterator.remove();

				break;
			}
		}

		this.currentScroll = 0.0F;
		containerExcludify.scrollTo(0.0F);
	}

	private boolean needsScrollBars()
	{
		return ItemHandler.getItemRegistrySize() > 72; //TODO
	}

	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0 && this.needsScrollBars())
		{
			final int j = ItemHandler.getItemRegistrySize() / 9 - 5 + 1;

			if (i > 0)
				i = 1;

			if (i < 0)
				i = -1;

			this.currentScroll = (float)(this.currentScroll - (double)i / (double)j);

			if (this.currentScroll < 0.0F)
				this.currentScroll = 0.0F;

			if (this.currentScroll > 1.0F)
				this.currentScroll = 1.0F;

			((ContainerExcludify)this.inventorySlots).scrollTo(this.currentScroll);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		if(this.onlyActive)
			RenderHelper.drawTexturedRect(GuiContainerExcludify.tabs, this.guiLeft+166, this.guiTop-30, 0, 2, 28, 30, 256, 256, 1F);
		else
			RenderHelper.drawTexturedRect(GuiContainerExcludify.tabs, this.guiLeft+166, this.guiTop+this.ySize-1, 0, 64, 28, 38, 256, 256, 1F);
		RenderHelper.drawTexturedRect(this.onlyActive ? GuiContainerExcludify.textureActive:GuiContainerExcludify.textureSearch, this.guiTop, this.guiLeft, 0, 0, 200, 200, this.xSize, this.ySize, 1F);
	}

}
