package me.superckl.dpu.common.utlilty;

import me.superckl.dpu.common.reference.ModData;

public class StringHelper {

	public static String formatGUIUnlocalizedName(final String name){
		return "gui."+ModData.MOD_ID.toLowerCase()+":"+name+".name";
	}

	public static String build(final Object ... objects){
		final StringBuilder builder = new StringBuilder();
		for(final Object object:objects)
			builder.append(object);
		return builder.toString();
	}

}
