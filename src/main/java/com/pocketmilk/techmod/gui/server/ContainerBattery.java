package com.pocketmilk.techmod.gui.server;

import com.pocketmilk.techmod.entities.BaseTile;
import com.pocketmilk.techmod.entities.TileBattery;
import com.pocketmilk.techmod.entities.TileGenerator;
import com.pocketmilk.techmod.gui.slots.SlotBurn;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBattery extends BaseContainer {
	
	public int burnTime = 0;
	public int totalBurnTime = 0;
	public long energy;
	public int tickTime;
	EntityPlayer player;
	TileBattery tile;

	public ContainerBattery(TileBattery tile, EntityPlayer player) {
		super();
		this.tile = tile;
		this.player = player;

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, 142));
		}
		

		//this.addSlotToContainer(new SlotCharge(tile.inventory, 0, 80, 17));
		//this.addSlotToContainer(new SlotCharge(tile.inventory, 1, 80, 53));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < this.crafters.size(); i++)
		{
			ICrafting icrafting = this.crafters.get(i);
			if (this.energy != (int) tile.getPower())
			{
				icrafting.sendProgressBarUpdate(this, 2, (int) tile.getPower());
			}
		}
	}
	
	@Override
	public void onCraftGuiOpened(ICrafting crafting)
	{
		super.onCraftGuiOpened(crafting);
		crafting.sendProgressBarUpdate(this, 2, (int) tile.getPower());
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value)
	{
		if (id == 0)
		{
			this.burnTime = value;
		} else if (id == 1)
		{
			this.totalBurnTime = value;
		} else if (id == 2)
		{
			this.energy = value;
		}
		this.tile.setPower(energy);
	}
	
	public int getScaledBurnTime(int i)
	{
		return (int) (((float) burnTime / (float) totalBurnTime) * i);
	}
	
}
