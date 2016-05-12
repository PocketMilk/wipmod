package com.pocketmilk.techmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class PocketBlocks {
	
	
	// Create blank local copy of EVERY block we add as a 'Block', variable name is only used to register block
	//public static Block testBlock;
	public static Block blockGeneratorCoal;
	public static Block blockBattery;
	public static Block blockWireBase;
	
	public static void initBlocks() {
		//testBlock = new BaseBlock("testBlock", Material.iron, 1, 1);
		
		// Construct every block we listed above as their final class type
		// Register every block via registerBlock, replace variable names as necessary
		// Don't forget to add an entry inside the TechModRender class or the block won't render properly!!!!
		//GameRegistry.registerBlock(testBlock, testBlock.getUnlocalizedName().substring(5));
		
		blockGeneratorCoal = new BlockGeneratorCoal();
		GameRegistry.registerBlock(blockGeneratorCoal, blockGeneratorCoal.getUnlocalizedName().substring(5));
	
		blockBattery = new BlockBattery();
		GameRegistry.registerBlock(blockBattery, blockBattery.getUnlocalizedName().substring(5));
		
		blockWireBase = new BlockWireBase();
		GameRegistry.registerBlock(blockWireBase, blockWireBase.getUnlocalizedName().substring(5));
	}
	
	public static void addRecipes() {
		((BaseMachine) blockGeneratorCoal).preInit();
		((BaseMachine) blockBattery).preInit();
		((BlockWireBase) blockWireBase).preInit();
	}
	
}