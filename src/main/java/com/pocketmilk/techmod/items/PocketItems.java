package com.pocketmilk.techmod.items;

import com.pocketmilk.techmod.blocks.BaseMachine;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class PocketItems {
	
	//public static Item testItem;
	public static Item bubbaItem;
	
	public static void initItems() {
		//testItem = new BaseItem("testItem");
		bubbaItem = new BubbaItem("bubbaItem");
		//testBlock.setRegistryName("testBlock");
		//GameRegistry.registerItem(testItem, testItem.getUnlocalizedName().substring(5));
		GameRegistry.registerItem(bubbaItem, bubbaItem.getUnlocalizedName().substring(5));
	}
	
	public static void addRecipes() {
		((BaseItem) bubbaItem).preInit();
	}
}