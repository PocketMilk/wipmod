package com.pocketmilk.techmod.gui.server;

import com.pocketmilk.techmod.entities.*;

import net.minecraft.entity.player.InventoryPlayer;
//import net.minecraft.tileentity.TileEntity;

public class ContainerFurnaceGen extends BaseContainer {
	
	TileGenerator generator;

	public ContainerFurnaceGen(InventoryPlayer inv, TileGenerator entity) {
		super((BaseTile)entity, 25, 36, "Fuel");
		generator = entity;
		
		addPlayerInventory(inv, 68);
	}
}