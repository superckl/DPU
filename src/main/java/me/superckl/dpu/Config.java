package me.superckl.dpu;

import java.io.File;

import lombok.Getter;
import me.superckl.dpu.common.reference.ModData;
import me.superckl.dpu.common.utlilty.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {

	@Getter
	private final Configuration configFile;
	@Getter
	private boolean allowDelete = true;
	@Getter
	private boolean noCreativeSearch;
	@Getter
	private boolean easyRecipe;
	@Getter
	private boolean versionCheck;
	@Getter
	private boolean hotbarOnly;
	@Getter
	private boolean syncNEISearch;
	@Getter
	private boolean allowNBTCopying;
	@Getter
	private boolean allowNBTClearing;

	public static final class Category{

		public static String GENERAL = "general";
		public static String NEI = "nei";

	}

	public Config(final File config){
		this.configFile = new Configuration(config);
		try{
			this.configFile.load();
		}catch(final Exception e){
			LogHelper.warn("Failed to load configuration! All options will be set to their default values.");
			e.printStackTrace();
		}finally{
			if(this.configFile.hasChanged())
				this.configFile.save();
		}
	}

	public void loadValues(){
		try{
			this.allowDelete = this.configFile.getBoolean("Allow Deletions", Category.GENERAL, true, "If true, players will be able to set excludifiers to delete certain items.");
			this.noCreativeSearch = this.configFile.getBoolean("Disable Search Tab", Category.GENERAL, false, "If true, the search tab will not appear in the excludifier GUI. This can be used if you feel it is unfair or over powered. If true, this value will be synced to the client.");
			this.easyRecipe = this.configFile.getBoolean("Easy Recipe", Category.GENERAL, false, "If true, DPU will use an easier version of the recipe to allow the excludifier to be obtained early-game. If this value is mismatched between client and server, recipe mods such as NEI will display an incorrect recipe.");
			this.versionCheck = this.configFile.getBoolean("Version Check", Category.GENERAL, true, "If true, DPU will attempt to contact Not Enough Mods to check if a newer version is available. (Requires internet connection)");
			this.hotbarOnly = this.configFile.getBoolean("Hotbar Only", Category.GENERAL, false, "If true, DPU will only search a player's hotbar for excludifiers. This value is taken from the server side.");
			this.allowNBTClearing = this.configFile.getBoolean("Allow NBT Clearing", Category.GENERAL, true, "If true, players will be able to clear NBT data from their excludifers.");
			this.allowNBTCopying = this.configFile.getBoolean("Allow NBT Copying", Category.GENERAL, true, "If true, players will be able to copy NBT from other excludifers.");
			if(Loader.isModLoaded("NotEnoughItems"))
				this.syncNEISearch = this.configFile.getBoolean("Synchronize NEI Search", Category.NEI, true, "If true, NEI's search bar will be synchornized with DPU's.");
			this.configFile.save();
		}catch(final Exception e){
			e.printStackTrace();
		}finally{
			if(this.configFile.hasChanged())
				this.configFile.save();
		}
	}

	public void setNoSearchOverride(){
		this.noCreativeSearch = true;
	}

	@SubscribeEvent
	public void onConfigChange(final OnConfigChangedEvent e){
		if(e.modID.equals(ModData.MOD_ID))
			this.loadValues();
	}

	public NBTTagCompound serialize(){
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("allowDelete", this.allowDelete);
		tag.setBoolean("noCreativeSearch", this.noCreativeSearch);
		return tag;
	}

	public void deserialize(final NBTTagCompound tag){
		if(tag.hasKey("allowDelete"))
			this.allowDelete = tag.getBoolean("allowDelete");
		if(tag.hasKey("noCreativeSearch"))
			this.noCreativeSearch = tag.getBoolean("noCreativeSearch");
	}

}
