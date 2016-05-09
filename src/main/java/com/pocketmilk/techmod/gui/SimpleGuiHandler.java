package com.pocketmilk.techmod.gui;

import com.pocketmilk.techmod.entities.*;
import com.pocketmilk.techmod.gui.server.*;
import com.pocketmilk.techmod.gui.client.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/*
 * Contains some code from cofh Core
 */

public class SimpleGuiHandler implements IGuiHandler {
	private int guiIdCounter = 1;
	public static int manualGUI = 0;
	
	
	public static final int coalGeneratorID = 1;
	public static final int batteryID = 2;


	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("getServerGuiElement");
		if (ID == coalGeneratorID)
		{
			return new ContainerFurnaceGen((TileGenerator) world.getTileEntity(new BlockPos(x, y, z)), player);
		} else if (ID == batteryID)
		{
			return new ContainerBattery((TileBattery) world.getTileEntity(new BlockPos(x, y, z)), player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("getClientGuiElement");
		
		if (ID == coalGeneratorID)
		{
			return new GUIFurnaceGen(player, (TileGenerator) world.getTileEntity(new BlockPos(x, y, z)));
		} else if (ID == batteryID)
		{
			return new GUIBattery(player, (TileBattery) world.getTileEntity(new BlockPos(x, y, z)));
		}
		

		return null;
	}


}
