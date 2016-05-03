package com.example.examplemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// This mod's imports
import com.example.examplemod.ref.Ref;


// Minecraft imports
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Ref.MODID, name = Ref.MODNAME, version = Ref.Version, dependencies = "after:CoFHAPI|energy;after:CoFHCore;")
public class ExampleMod
{
	@Instance(Ref.MODID)
	public static ExampleMod instance;
	
	@SidedProxy(clientSide = "com.example."+Ref.MODID+".core.ClientProxy", serverSide = "com.example."+Ref.MODID+".core.Proxy")
	public static Proxy proxy;
    
    @EventHandler // - Pre-initialization, create content
    public void preInit(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    
    @EventHandler // Initialization, recipe registration
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
    
    @EventHandler // Post-initialization, not used for anything
    public void postInit(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
