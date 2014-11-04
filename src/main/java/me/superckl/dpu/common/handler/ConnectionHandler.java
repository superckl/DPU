package me.superckl.dpu.common.handler;

import me.superckl.dpu.DPUMod;
import me.superckl.dpu.common.network.MessageConfigSync;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.utlilty.LogHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class ConnectionHandler{

	@SubscribeEvent(receiveCanceled = false)
	public void onPlayerLogin(final PlayerLoggedInEvent e){
		if(e.player.worldObj.isRemote)
			return;
		if(e.player instanceof EntityPlayerMP)
			ModData.CONFIG_SYNC_CHANNEL.sendTo(new MessageConfigSync(DPUMod.getInstance().getConfig().serialize()), (EntityPlayerMP) e.player);
	}

	@SubscribeEvent(receiveCanceled = false)
	public void onServerDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent e){
		LogHelper.info("Restoring client config values...");
		DPUMod.getInstance().getConfig().loadValues();
	}

}
