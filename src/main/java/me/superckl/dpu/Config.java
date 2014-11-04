package me.superckl.dpu;

import java.io.File;

import lombok.Getter;
import me.superckl.dpu.common.reference.ModData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {

	@Getter
	private final Configuration configFile;

	@Getter
	private boolean justDelete;
	@Getter
	private boolean noCreativeSearch;

	public static final class Category{

		public static String GENERAL = "general";

	}

	public Config(final File config){
		this.configFile = new Configuration(config);
		try{
			this.configFile.load();
		}catch(final Exception e){
			e.printStackTrace();
		}finally{
			if(this.configFile.hasChanged())
				this.configFile.save();
		}
	}

	public void loadValues(){
		try{
			this.justDelete = this.configFile.getBoolean("Just Delete It", Category.GENERAL, false, "If true, excludifiers will just delete items that are excluded. Use this if you are noticing lag or are running a server with many players.");
			this.noCreativeSearch = this.configFile.getBoolean("Disable Search Tab", Category.GENERAL, false, "If true, the search tab will not appear in the excludifier GUI. This cna be used if you feel it is unfair or over powered. If true, this value will be synced to the client.");
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
		tag.setBoolean("justDelete", this.justDelete);
		tag.setBoolean("noCreativeSearch", this.noCreativeSearch);
		return tag;
	}

	public void deserialize(final NBTTagCompound tag){
		if(tag.hasKey("justDelete"))
			this.justDelete = tag.getBoolean("justDelete");
		if(tag.hasKey("noCreativeSearch"))
			this.noCreativeSearch = tag.getBoolean("noCreativeSearch");
	}

}
