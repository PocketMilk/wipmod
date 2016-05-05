package com.pocketmilk.techmod.render;

import com.pocketmilk.techmod.blocks.PocketBlocks;
import com.pocketmilk.techmod.items.PocketItems;
import com.pocketmilk.techmod.TechMod;
import com.pocketmilk.techmod.ref.Ref;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public final class TechModRender {
	
	public static void registerBlockRender() {
		
		//registerBlock(PocketBlocks.testBlock);
		registerBlock(PocketBlocks.blockGeneratorCoal);
	}
	
	public static void registerItemRender() {
		
		//registerItem(PocketItems.testItem);
		registerItem(PocketItems.bubbaItem);
	}
	
	public static void registerBlock(Block block) {
		if (TechMod.proxy.isClient()) {
			Item item = Item.getItemFromBlock(block);
			if(item != null) { 
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Ref.MODID + ":" +item.getUnlocalizedName().substring(5), "inventory"));
			}
		}
	}
	
	public static void registerItem(Item item) {
		if (TechMod.proxy.isClient()) {
			if(item != null) { 
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Ref.MODID + ":" +item.getUnlocalizedName().substring(5), "inventory"));
			}
		}
	}
	
}