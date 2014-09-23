package me.superckl.dpu.server.handler;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.network.MessageNoSearch;
import me.superckl.dpu.common.reference.ModData;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class ConnectionHandler{

	@SubscribeEvent(receiveCanceled = false)
	public void onPlayerLogin(final PlayerLoggedInEvent e){
		if(e.player.worldObj.isRemote)
			return;
		if(e.player instanceof EntityPlayerMP && DPUMod.getInstance().getConfig().isNoCreativeSearch())
			ModData.GUI_UPDATE_CHANNEL.sendTo(new MessageNoSearch(), (EntityPlayerMP) e.player);
	}

}
