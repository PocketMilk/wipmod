package com.pocketmilk.techmod.items;

import net.minecraft.item.Item;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BaseItem extends Item {
	
	public BaseItem(String name) {
		super();
		
		this.setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	public void preInit() {
		this.addRecipe();
	}

	abstract void addRecipe();
	
}