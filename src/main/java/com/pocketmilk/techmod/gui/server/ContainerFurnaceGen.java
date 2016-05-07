package com.pocketmilk.techmod.gui.server;

import com.pocketmilk.techmod.entities.BaseTile;
import com.pocketmilk.techmod.entities.TileGenerator;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerFurnaceGen extends BaseContainer {
	
	TileGenerator generator;

	public ContainerFurnaceGen(InventoryPlayer inv, TileEntity entity) {
		super((BaseTile)entity, 25, 36, "Fuel");
		generator = (TileGenerator)entity;
		
		addPlayerInventory(inv, 68);
	}
}