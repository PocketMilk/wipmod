package com.pocketmilk.techmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




// This mod's imports
import com.pocketmilk.techmod.ref.Ref;
import com.pocketmilk.techmod.core.Proxy;
import com.pocketmilk.techmod.blocks.PocketBlocks;
//import com.example.examplemod.items.ExampleItems;




// Minecraft imports
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.Version, dependencies = "after:CoFHAPI|energy;after:CoFHCore;")
public class TechMod
{
	@Instance(Ref.MODID)
	public static TechMod instance;
	
	@SidedProxy(clientSide = "com.pocketmilk."+Ref.MODID+".core.ClientProxy", serverSide = "com.pocketmilk."+Ref.MODID+".core.Proxy")
	public static Proxy proxy;
	
	//logger
	public static final Logger logger = LogManager.getLogger(Ref.MODID);
	
	public TechMod() {
		logger.info("Starting Pocketmilk 1.0");
	}
    
    @EventHandler // - Pre-initialization, create content
    public void preInit(FMLPreInitializationEvent event)
    {
		this.proxy.preInit(event);
		//MinecraftForge.EVENT_BUS.register(new EventPlayers());
    }
    
    @EventHandler // Initialization, recipe registration
    public void init(FMLInitializationEvent event)
    {
		this.proxy.init(event);
		FMLCommonHandler.instance().bus().register(instance);
		//proxy.init();
    }
    
    @EventHandler // Post-initialization, not used for anything
    public void postInit(FMLPostInitializationEvent event)
    {
    	this.proxy.postInit(event);
    }
}
