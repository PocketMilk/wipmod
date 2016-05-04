package com.pocketmilk.techmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class PocketBlocks {
	
	public static Block testBlock;
	
	public static void initBlocks() {
		testBlock = new BaseBlock("testBlock", Material.iron, 1, 1);
		//testBlock.setRegistryName("testBlock");
		GameRegistry.registerBlock(testBlock, testBlock.getUnlocalizedName().substring(5));
	}
	
}

