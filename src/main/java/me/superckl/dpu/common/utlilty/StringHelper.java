package me.superckl.dpu.common.utlilty;

import me.superckl.dpu.common.reference.ModData;

public class StringHelper {

	public static String formatGUIUnlocalizedName(final String name){
		return "gui."+ModData.MOD_ID.toLowerCase()+":"+name+".name";
	}

}
