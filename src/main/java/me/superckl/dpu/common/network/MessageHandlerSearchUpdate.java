package me.superckl.dpu.common.network;

import java.util.Iterator;

import me.superckl.dpu.common.container.ContainerExcludify;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MessageHandlerSearchUpdate implements IMessageHandler<MessageSearchUpdate, IMessage>{

	@SideOnly(Side.CLIENT)
	private final Minecraft mc;

	@SideOnly(Side.CLIENT)
	public MessageHandlerSearchUpdate() {
		this.mc = Minecraft.getMinecraft();
	}

	@Override
	public IMessage onMessage(final MessageSearchUpdate message, final MessageContext ctx) {
		this.updateFilteredItems((ContainerExcludify) ctx.getServerHandler().playerEntity.openContainer, message.isOnlyActive(), message.getText());
		return null;
	}

	private void updateFilteredItems(final ContainerExcludify containerExcludify, final boolean onlyActive, final String s1)
	{
		if(!onlyActive)
			containerExcludify.refreshCreativeList();
		else
			containerExcludify.refreshActiveList();
		Iterator iterator;
		final Enchantment[] aenchantment = Enchantment.enchantmentsList;
		final int j = aenchantment.length;

		if(!onlyActive)
			for (int i = 0; i < j; ++i)
			{
				final Enchantment enchantment = aenchantment[i];

				if (enchantment != null && enchantment.type != null)
					Items.enchanted_book.func_92113_a(enchantment, containerExcludify.itemList);
			}

		iterator = containerExcludify.itemList.iterator();

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

		containerExcludify.scrollTo(0.0F);
	}

}
