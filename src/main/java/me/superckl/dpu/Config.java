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

@Getter
public class Config {

	private final Configuration configFile;
	private boolean allowDelete = true;
	private boolean noCreativeSearch;
	private boolean easyRecipe;
	private boolean versionCheck;
	private boolean hotbarOnly;
	private boolean syncNEISearch;
	private boolean allowNBTCopying;
	private boolean allowNBTClearing;
	private int trackTimer;
	private boolean shortenLife;
	private int shortenLifeTo;

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
			this.trackTimer = this.configFile.getInt("Item Track Timer", Category.GENERAL, 20, 0, Integer.MAX_VALUE, "Determines how long DPU will wait before searching a player's inventory again. i.e. If set to 20, when DPU blocks an item pick-up, it will wait 20 ticks before again searching the player's inventory for excludifers. During this time, the pickup will be blocked by UUID.");
			this.shortenLife = this.configFile.getBoolean("Shorten Lifespan", Category.GENERAL, false, "If true, DPU will shorten the time for an item entity to despawn if it denies the pickup.");
			this.shortenLifeTo = this.configFile.getInt("Shoten Lifespan To", Category.GENERAL, 600, 0, Integer.MAX_VALUE, "The value to shorten an item entity's remaining time. This is measured in ticks and is only applied if Shorten Lifespan is true.");
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
