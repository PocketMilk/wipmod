package com.pocketmilk.techmod.core;

import com.pocketmilk.techmod.render.TechModRender;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


public class ClientProxy extends Proxy {

	@Override
	public void registerEntities() {
		super.registerEntities();
	}
	
	@Override
	public boolean isClient() {
		return true;
	}
	
	@Override
	public boolean isServer() {
		return false;
	}
	
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	
	public void init(FMLInitializationEvent event) {
		super.init(event);
		TechModRender.registerBlockRender();
		TechModRender.registerItemRender();
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}