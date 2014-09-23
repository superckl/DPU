package me.superckl.dpu;

import lombok.Getter;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.proxy.IProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION, guiFactory=ModData.GUI_FACTORY)
public class DPUMod {

	@Instance(ModData.MOD_ID)
	@Getter
	private static DPUMod instance;

	@SidedProxy(clientSide=ModData.CLIENT_PROXY, serverSide=ModData.SERVER_PROXY)
	@Getter
	private static IProxy proxy;

	@Getter
	private Config config;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent e){
		this.config = new Config(e.getSuggestedConfigurationFile());
		this.config.loadValues();
		ModItems.init();
		DPUMod.proxy.registerHandlers();
	}

	@EventHandler
	public void init(final FMLInitializationEvent e){

	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent e){
		ItemHandler.countItems();
	}

}
