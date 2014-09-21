package me.superckl.dpu;

import java.util.Iterator;

import net.minecraft.item.Item;
import net.minecraft.util.RegistryNamespaced;

public class ItemHandler {

	private static int size;

	public static RegistryNamespaced getAllItems(){
		return Item.itemRegistry;
	}

	public static int getItemRegistrySize(){
		if(ItemHandler.size == 0)
			ItemHandler.countItems();
		return ItemHandler.size;
	}

	public static int countItems(){
		ItemHandler.size = 0;
		final Iterator it = ItemHandler.getAllItems().iterator();
		while(it.hasNext()){
			ItemHandler.size++;
			it.next();
		}
		return ItemHandler.size;
	}

}
