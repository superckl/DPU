package me.superckl.dpu;

import lombok.Getter;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.reference.ModItems;
import me.superckl.dpu.common.utlilty.LogHelper;
import me.superckl.dpu.common.utlilty.VersionChecker;
import me.superckl.dpu.proxy.IProxy;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=ModData.MOD_ID, name=ModData.MOD_NAME, version=ModData.VERSION, guiFactory=ModData.GUI_FACTORY, canBeDeactivated = true)
public class DPUMod {

	@Instance(ModData.MOD_ID)
	@Getter
	private static DPUMod instance;

	@SidedProxy(clientSide=ModData.CLIENT_PROXY, serverSide=ModData.SERVER_PROXY)
	@Getter
	private static IProxy proxy;

	@Getter
	private Config config;

	@Getter
	private VersionChecker versionChecker;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent e){
		LogHelper.info("Please note, you are running a beta version! Please report any bugs you find.");
		this.config = new Config(e.getSuggestedConfigurationFile());
		this.config.loadValues();
		if(this.config.isVersionCheck())
			this.versionChecker = VersionChecker.start(ModData.MOD_ID, ModData.VERSION, MinecraftForge.MC_VERSION);
		ModItems.init();
		DPUMod.proxy.registerHandlers();
		DPUMod.proxy.registerBindings();
	}

	@EventHandler
	public void init(final FMLInitializationEvent e){}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent e){
		ItemHandler.countItems();
		LogHelper.info("Successfully enabled!");
	}

}
