package com.pocketmilk.techmod.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BubbaItem extends BaseItem {
	
	public BubbaItem(String name) {
		super(name);
		
		//setUnlocalizedName(name);
	}
	
	public void addRecipe() {
		GameRegistry.addRecipe(new ItemStack(PocketItems.bubbaItem),
				"AAA",
				"AAA",
				"AAA",
				'A', Blocks.cobblestone
		);
	}
}
