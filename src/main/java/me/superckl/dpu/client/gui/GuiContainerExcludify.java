package me.superckl.dpu.client.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.container.ContainerExcludify;
import me.superckl.dpu.common.container.SlotDisplay;
import me.superckl.dpu.common.container.SlotSearch;
import me.superckl.dpu.common.network.MessageNoSearch;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.LogHelper;
import me.superckl.dpu.common.utlilty.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class GuiContainerExcludify extends GuiContainer{

	private static final ResourceLocation textureSearch = new ResourceLocation(ModData.MOD_ID+":textures/gui/excludifiersearch.png");
	private static final ResourceLocation textureActive = new ResourceLocation(ModData.MOD_ID+":textures/gui/excludifieractive.png");
	private static final ResourceLocation tabs = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

	private static final ItemStack compass = new ItemStack(Items.compass);
	private static final ItemStack excludifier = new ItemStack(ModItems.excludifier);

	private final EntityPlayer player;
	private GuiTextField textField;
	private float currentScroll;
	private boolean onlyActive = true;
	private boolean wasClicking;
	private boolean isScrolling;
	private String NEIWord;

	public GuiContainerExcludify(final EntityPlayer player) {
		super(new ContainerExcludify(player));
		this.player = player;
		this.xSize = 195;
		this.ySize = 168;
	}

	@Override
	public void initGui() {
		super.initGui();
		final int xStart = (this.width - this.xSize) / 2;
		final int yStart = (this.height - this.ySize) / 2;
		this.buttonList.clear();
		this.textField = new GuiTextField(this.fontRendererObj, xStart+82, yStart+6, 87, 9);
		this.textField.setMaxStringLength(15);
		this.textField.setEnableBackgroundDrawing(false);
		this.textField.setTextColor(16777215);
		this.refreshTextfield();
	}

	private void refreshTextfield(){
		this.focusTextfield();
		this.clearTextfield();
	}

	private void clearTextfield(){
		this.textField.setText("");
	}

	private void focusTextfield(){
		this.textField.setVisible(true);
		this.textField.setCanLoseFocus(false);
		this.textField.setFocused(true);
	}

	private void unfocusTextfield(){
		this.textField.setVisible(true);
		this.textField.setCanLoseFocus(true);
		this.textField.setFocused(false);
	}

	@Override
	protected void keyTyped(final char p_73869_1_, final int p_73869_2_)
	{

		if (!this.checkHotbarKeys(p_73869_2_))
			if (this.textField.textboxKeyTyped(p_73869_1_, p_73869_2_))
				this.updateSearch();
			else
				super.keyTyped(p_73869_1_, p_73869_2_);
	}

	@Override
	protected void handleMouseClick(final Slot slot, final int par2, final int par3, final int par4)
	{
		if(slot == null || slot instanceof SlotSearch == false){
			if(slot != null && slot.getHasStack() && slot.getStack() == this.player.getHeldItem()){
				this.player.closeScreen();
				return;
			}
			super.handleMouseClick(slot, par2, par3, par4);
			return;
		}
		final SlotSearch slotS = (SlotSearch) slot;
		if(slotS.onClick(this.player, this.player.inventory.getItemStack(), !this.onlyActive, par3 == 0)){
			this.currentScroll = 0F;
			this.textField.setText("");
		}
	}

	public void onNoSearchReceived(final MessageNoSearch message){
		final ContainerExcludify containerExcludify = (ContainerExcludify) this.inventorySlots;
		if(!this.onlyActive){
			this.onlyActive = true;
			containerExcludify.addActiveSlots();
			this.currentScroll = 0F;
			this.textField.setText("");
		}
		if(message.getId() != -1)
			((SlotDisplay)containerExcludify.inventorySlots.get(message.getId())).onClick(this.player, null, !this.onlyActive, true);
	}

	@Override
	protected void mouseMovedOrUp(final int mouseX, final int mouseY, final int action)
	{
		super.mouseMovedOrUp(mouseX, mouseY, action);
		if (action == 0 && !this.isScrolling)
		{
			final int xStart = (this.width - this.xSize) / 2;
			final int yStart = (this.height - this.ySize) / 2;
			final int x = mouseX - xStart;
			final int y = mouseY - yStart;
			if(x < 167 || x > 195)
				return;
			final ContainerExcludify containerExcludify = (ContainerExcludify) this.inventorySlots;
			if(y < 0 && !DPUMod.getInstance().getConfig().isNoCreativeSearch() && this.onlyActive){
				this.onlyActive = false;
				containerExcludify.addSearchSlots();
				this.currentScroll = 0F;
				containerExcludify.scrollTo(0F);
				this.textField.setText("");
			}else if(y > 168 && !this.onlyActive){
				this.onlyActive = true;
				containerExcludify.addActiveSlots();
				this.currentScroll = 0F;
				containerExcludify.scrollTo(0F);
				this.textField.setText("");
			}
		}
	}

	private void updateSearch()
	{
		final ContainerExcludify containerExcludify = (ContainerExcludify) this.inventorySlots;
		if(!this.onlyActive)
			containerExcludify.refreshCreativeList();
		else
			containerExcludify.refreshActiveList();
		this.updateFilteredItems(containerExcludify);
		try{
			if (DPUMod.getInstance().getConfig().isSyncNEISearch() && this.NEIWord == null || !this.NEIWord.equals(this.textField.getText()))
			{
				final Class c = ReflectionHelper.getClass(this.getClass().getClassLoader(), "codechicken.nei.LayoutManager");
				final Field fldSearchField = c.getField("searchField");
				final Object searchField = fldSearchField.get(c);

				final Method a = searchField.getClass().getMethod("setText", String.class);
				final Method b = searchField.getClass().getMethod("onTextChange", String.class);

				this.NEIWord = this.textField.getText();
				a.invoke(searchField, this.textField.getText());
				b.invoke(searchField, "");
			}
		}catch (final Throwable ignore){
			LogHelper.debug("An error was caught while trying to update NEI search! You should disable search syncing if this persists (and report it).");
			LogHelper.debug(ignore.toString());
		}
	}

	private void updateFilteredItems(final ContainerExcludify containerExcludify)
	{
		Iterator iterator;
		final Enchantment[] aenchantment = Enchantment.enchantmentsList;
		final int j = aenchantment.length;

		if(!this.onlyActive)
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
		final ContainerExcludify containerExcludify = (ContainerExcludify) this.inventorySlots;
		return containerExcludify.itemList.size() > (this.onlyActive ? 27:72);
	}

	@Override
	public void handleMouseInput()
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0 && this.needsScrollBars())
		{
			final ContainerExcludify containerExcludify = (ContainerExcludify) this.inventorySlots;
			final int j = containerExcludify.itemList.size() / 9 - (this.onlyActive ? 3:8) + 1;

			if (i > 0)
				i = 1;

			if (i < 0)
				i = -1;

			this.currentScroll = (float)(this.currentScroll - (double)i / (double)j);

			if (this.currentScroll < 0.0F)
				this.currentScroll = 0.0F;

			if (this.currentScroll > 1.0F)
				this.currentScroll = 1.0F;

			containerExcludify.scrollTo(this.currentScroll);
		}
	}



	@Override
	public void drawScreen(final int p_73863_1_, final int p_73863_2_, final float p_73863_3_) {
		final boolean flag = Mouse.isButtonDown(0);
		final int k = this.guiLeft;
		final int l = this.guiTop;
		final int i1 = k + 175;
		final int j1 = l + 18;
		final int k1 = i1 + 12;
		final int l1 = j1 + (this.onlyActive ? 52:142);

		if (!this.wasClicking && flag && p_73863_1_ >= i1 && p_73863_2_ >= j1 && p_73863_1_ < k1 && p_73863_2_ < l1)
			this.isScrolling = this.needsScrollBars();

		if (!flag)
			this.isScrolling = false;

		this.wasClicking = flag;

		if (this.isScrolling)
		{
			this.currentScroll = (p_73863_2_ - j1 - 7.5F) / (l1 - j1 - 15.0F);

			if (this.currentScroll < 0.0F)
				this.currentScroll = 0.0F;

			if (this.currentScroll > 1.0F)
				this.currentScroll = 1.0F;

			((ContainerExcludify)this.inventorySlots).scrollTo(this.currentScroll);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int p_146979_1_, final int p_146979_2_) {
		GL11.glEnable(GL11.GL_BLEND);
		int lastIndex = -2;
		for(final Object obj:this.inventorySlots.inventorySlots){
			if(obj instanceof SlotSearch == false)
				continue;
			final SlotSearch slot = (SlotSearch) obj;
			if(slot.isSelected() && slot.getHasStack()){
				RenderHelper.drawTexturedRect(GuiContainerExcludify.textureActive, slot.xDisplayPosition-3, slot.yDisplayPosition-3, 1000, 195, 0, 22, 22, 256, 256, 1F);
				SlotSearch s;
				final boolean left = slot.slotNumber % 9 != 0 && (s = (SlotSearch)this.inventorySlots.getSlot(slot.slotNumber - 1)).isSelected() && s.getHasStack();
				final boolean right = slot.slotNumber % 9 != 8  && (s = (SlotSearch)this.inventorySlots.getSlot(slot.slotNumber + 1)).isSelected() && s.getHasStack();
				final boolean top = slot.slotNumber-9 >= 0 && (s = (SlotSearch)this.inventorySlots.inventorySlots.get(slot.slotNumber-9)).isSelected() && s.getHasStack();
				final boolean topLeft = slot.slotNumber-9 >= 0 && slot.slotNumber % 9 != 0 && (s = (SlotSearch)this.inventorySlots.inventorySlots.get(slot.slotNumber-10)).isSelected() && s.getHasStack();
				final boolean topRight = slot.slotNumber-9 >= 0 && slot.slotNumber % 9 != 8 && (s = (SlotSearch)this.inventorySlots.inventorySlots.get(slot.slotNumber-8)).isSelected() && s.getHasStack();
				if(left)
					RenderHelper.drawTexturedRect(GuiContainerExcludify.textureActive, slot.xDisplayPosition-3, slot.yDisplayPosition-3, 1000, 195, 22, 5, 22, 256, 256, 1F);
				if(top)
					RenderHelper.drawTexturedRect(GuiContainerExcludify.textureActive, slot.xDisplayPosition-2, slot.yDisplayPosition-3, 1000, 195, 44, 20, 4, 256, 256, 1F);
				if(!left && topLeft)
					RenderHelper.drawTexturedRect(GuiContainerExcludify.textureActive, slot.xDisplayPosition-3, slot.yDisplayPosition-3, 1000, 195, 52, 4, 4, 256, 256, 1F);
				if(!right && topRight)
					RenderHelper.drawTexturedRect(GuiContainerExcludify.textureActive, slot.xDisplayPosition+15, slot.yDisplayPosition-3, 1000, 195, 48, 5, 4, 256, 256, 1F);
				if(slot.isDelete()){
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					this.zLevel = 300F;
					GuiScreen.itemRender.zLevel = 300F;
					this.fontRendererObj.drawString("x", slot.xDisplayPosition, slot.yDisplayPosition+8, 0xFF0000);
					GuiScreen.itemRender.zLevel = 0F;
					this.zLevel = 0F;
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glColor3f(1F, 1F, 1F);
				}
			}
			lastIndex = slot.slotNumber % 9;
		}
		GL11.glDisable(GL11.GL_BLEND);
		if(!this.onlyActive)
			this.fontRendererObj.drawString("Search Items", 8, 6, 0x404040);
		else{
			this.fontRendererObj.drawString("Selected Items", 7, 6, 0x404040);
			this.fontRendererObj.drawString("Inventory", 8, 74, 0x404040);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float p_146976_1_, final int p_146976_2_, final int p_146976_3_) {
		final int xStart = (this.width - this.xSize) / 2;
		final int yStart = (this.height - this.ySize) / 2;
		if(this.onlyActive && !DPUMod.getInstance().getConfig().isNoCreativeSearch())
			RenderHelper.drawTexturedRect(GuiContainerExcludify.tabs, xStart+195-28, yStart-28, 0, 2, 28, 30, 256, 256, 1F);
		else if(!this.onlyActive)
			RenderHelper.drawTexturedRect(GuiContainerExcludify.tabs, xStart+195-28, yStart+168-1, 0, 64, 28, 28, 256, 256, 1F);
		RenderHelper.drawTexturedRect(this.onlyActive ? GuiContainerExcludify.textureActive:GuiContainerExcludify.textureSearch, xStart, yStart, 0, 0, this.xSize, this.ySize, 256, 256, 1F);
		if(this.onlyActive)
			RenderHelper.drawTexturedRect(GuiContainerExcludify.tabs, xStart+195-28, yStart+164, 140, 96, 28, 32, 256, 256, 1F);
		else if(!DPUMod.getInstance().getConfig().isNoCreativeSearch())
			RenderHelper.drawTexturedRect(GuiContainerExcludify.tabs, xStart+195-28, yStart-28, 140, 32, 28, 32, 256, 256, 1F);
		net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		if(!DPUMod.getInstance().getConfig().isNoCreativeSearch())
			GuiScreen.itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), GuiContainerExcludify.compass, xStart+195-22, yStart-20);
		GuiScreen.itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), GuiContainerExcludify.excludifier, xStart+195-22, yStart+168+4);
		GL11.glDisable(GL11.GL_LIGHTING);
		this.textField.drawTextBox();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.drawTexturedRect(GuiContainerExcludify.textureActive, xStart+175, yStart+18+this.currentScroll*((this.onlyActive ? 52:142)-15), 195, this.needsScrollBars() ? 56:71, 12, 15, 256, 256, 1F);

	}

}
