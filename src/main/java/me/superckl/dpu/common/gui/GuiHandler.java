package me.superckl.dpu.common.gui;

import me.superckl.dpu.client.gui.GuiContainerExcludify;
import me.superckl.dpu.common.container.ContainerExcludify;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world,
			final int x, final int y, final int z) {
		switch(ID){
		case 0:
		{
			return new ContainerExcludify(player);
		}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world,
			final int x, final int y, final int z) {
		switch(ID){
		case 0:
		{
			return new GuiContainerExcludify(player);
		}
		}
		return null;
	}

}
