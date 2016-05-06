package com.pocketmilk.techmod.core;


//import net.minecraftforge.fml.common.registry.GameRegistry;
import com.pocketmilk.techmod.TechMod;
import com.pocketmilk.techmod.blocks.PocketBlocks;
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
		
	}
	
	public int registerGui(String guiName) {
		System.out.println("registerGui called.");
		return registerGui(guiName, guiName);
	}
	
	public int registerGui(String guiName, String containerName) {
		Class<?> gui = null;
		Class<?> container = null;
		System.out.println("Register GUI called with name " + guiName);
		try {
			gui = Proxy.class.getClassLoader().loadClass("com.pocketmilk.techmod.gui.client.GUI" + guiName);
		} catch (ClassNotFoundException e) {
			System.out.println("Client GUI not found.");
		}
		try {
			container = Proxy.class.getClassLoader().loadClass("com.pocketmilk.techmod.gui.server.Container" + containerName);
		} catch (ClassNotFoundException e) {
			System.out.println("Server GUI not found.");
			return -1;
		}
		if (gui == null) {
			System.out.println("Registering server GUI only");
			return TechMod.guiHandler.registerServerGui(container);
		} else {
			System.out.println("Registering client GUI");
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
