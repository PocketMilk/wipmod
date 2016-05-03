package com.example.examplemod.core;

import com.example.examplemod.ExampleMod;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class Proxy {

	public void registerEntities() {
		//miner
		//GameRegistry.registerTileEntity(TileMiner.class, "TileMiner");
		
	}
	
	public int registerGui(String guiName) {
		//return registerGui(guiName, guiName);
		return 0;
	}
	
	public int registerGui(String guiName, String containerName) {
		/*Class<?> gui = null;
		Class<?> container = null;
		try {
			gui = Proxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.gui.client.GUI" + guiName);
		} catch (ClassNotFoundException e) {
			
		}
		try {
			container = Proxy.class.getClassLoader().loadClass("com.vanhal.progressiveautomation.gui.container.Container" + containerName);
		} catch (ClassNotFoundException e) {
			return -1;
		}
		if (gui == null) {
			return ProgressiveAutomation.guiHandler.registerServerGui(container);
		} else {
			return ProgressiveAutomation.guiHandler.registerGui(gui, container);
		}*/
		return 0;
	}
	
	public boolean isClient() {
		return false;
	}
	
	public boolean isServer() {
		return true;
	}

	
	public void preInit() {
		
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		
	}
}
