package com.pocketmilk.techmod.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class PocketItems {
	
	public static Item testItem;
	
	public static void initItems() {
		testItem = new BaseItem("testItem");
		//testBlock.setRegistryName("testBlock");
		GameRegistry.registerItem(testItem, testItem.getUnlocalizedName().substring(5));
	}
	
}