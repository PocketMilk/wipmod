package com.pocketmilk.techmod.core;


//import net.minecraftforge.fml.common.registry.GameRegistry;
import com.pocketmilk.techmod.TechMod;
import com.pocketmilk.techmod.blocks.PocketBlocks;
import com.pocketmilk.techmod.entities.TileBattery;
import com.pocketmilk.techmod.entities.TileGenerator;
import com.pocketmilk.techmod.items.PocketItems;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Proxy {

	public void registerEntities() {
		//miner
		//GameRegistry.registerTileEntity(TileMiner.class, "TileMiner");
		GameRegistry.registerTileEntity(TileGenerator.class, "TileGen");
		GameRegistry.registerTileEntity(TileBattery.class, "TileBattery");
		
	}
	
	public int registerGui(String guiName) {
		return registerGui(guiName, guiName);
	}
	
	
	// This is what registers a GUI class based on the string you passed to it from the block.
	// The combined string of GUI+"Name"/Container+"Name" -HAS- to match the GUI class names for both client and server
	public int registerGui(String guiName, String containerName) {
		Class<?> gui = null;
		Class<?> container = null;
		try {
			gui = Proxy.class.getClassLoader().loadClass("com.pocketmilk.techmod.gui.client.GUI" + guiName);
		} catch (ClassNotFoundException e) {
		}
		try {
			container = Proxy.class.getClassLoader().loadClass("com.pocketmilk.techmod.gui.server.Container" + containerName);
		} catch (ClassNotFoundException e) {
			return -1;
		}
		if (gui == null) {
			return TechMod.guiHandler.registerServerGui(container);
		} else {
			return TechMod.guiHandler.registerGui(gui, container);
		}
		
	}
	
	public boolean isClient() {
		return false;
	}
	
	public boolean isServer() {
		return true;
	}

	
	public void preInit(FMLPreInitializationEvent event) {
		PocketBlocks.initBlocks();
		PocketItems.initItems();
		PocketBlocks.addRecipes();
		PocketItems.addRecipes();
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		registerEntities();
	}
}
