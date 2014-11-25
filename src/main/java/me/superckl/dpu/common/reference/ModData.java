package me.superckl.dpu.common.reference;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ModData {

	public static final String MOD_ID = "DPU";
	public static final String MOD_NAME= "Don't Pick Up";
	public static final String VERSION = "0.3-Beta";
	public static final String GUI_FACTORY = "me.superckl.dpu.common.gui.GuiFactory";
	public static final String CLIENT_PROXY = "me.superckl.dpu.proxy.ClientProxy";
	public static final String SERVER_PROXY = "me.superckl.dpu.proxy.ServerProxy";

	public static SimpleNetworkWrapper GUI_UPDATE_CHANNEL;
	public static SimpleNetworkWrapper ITEM_DELETE_CHANNEL;
	public static SimpleNetworkWrapper CONFIG_SYNC_CHANNEL;

}
