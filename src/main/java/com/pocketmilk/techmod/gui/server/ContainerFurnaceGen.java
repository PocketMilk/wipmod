package com.pocketmilk.techmod.gui.server;

import com.pocketmilk.techmod.entities.*;
import com.pocketmilk.techmod.gui.slots.SlotBurn;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFurnaceGen extends BaseContainer {
	public int burnTime = 0;
	public int totalBurnTime = 0;
	public long energy;
	public int tickTime;
	EntityPlayer player;
	TileGenerator tile;
	
	private int playerInvStart = 75;
	private int playerBarStart = 133;

	public ContainerFurnaceGen(TileGenerator tile, EntityPlayer player) {
		super();
		this.tile = tile;
		this.player = player;

		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new BaseSlot(player.inventory, j + i * 9 + 9, 8 + j * 18, playerInvStart + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new BaseSlot(player.inventory, i, 8 + i * 18, playerBarStart));
		}
		
		this.addSlotToContainer(new SlotBurn(tile, 0, 25, 45));
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
			if (this.burnTime != (int) tile.getBurnTime())
			{
				icrafting.sendProgressBarUpdate(this, 0, (int) tile.getBurnTime());
			}
			if (this.totalBurnTime != (int) tile.getTotalBurnTime())
			{
				icrafting.sendProgressBarUpdate(this, 1, (int) tile.getTotalBurnTime());
			}
		}
	}
	
	@Override
	public void onCraftGuiOpened(ICrafting crafting)
	{
		super.onCraftGuiOpened(crafting);
		crafting.sendProgressBarUpdate(this, 2, (int) tile.getPower());
		crafting.sendProgressBarUpdate(this, 1, (int) tile.getTotalBurnTime());
		crafting.sendProgressBarUpdate(this, 0, (int) tile.getBurnTime());
		//System.out.println("Kappa " + tile.getBurnTime());
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
		this.tile.setBurnTime(burnTime);
		this.tile.setTotalBurnTime(totalBurnTime);
	}
	
	public int getScaledBurnTime(int i)
	{
		return (int) (((float) burnTime / (float) totalBurnTime) * i);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		
		if (slotObject!=null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            
            if (slot < this.tile.getSizeInventory()) {
                if (!this.mergeItemStack(stackInSlot, this.tile.getSizeInventory(), inventorySlots.size(), true)) {
                	return null;
                }
            } else {
            	boolean foundSlot = false;
            	for (Object targetSlot: inventorySlots) {
            		if (targetSlot instanceof Slot) {
            			Slot theTargetSlot = (Slot) targetSlot;
            			int slotNum = theTargetSlot.slotNumber;
            			if (slot!=slotNum) {
	            			if ( (slotNum >= this.tile.SLOT_INVENTORY_START) && (this.tile.SLOT_INVENTORY_START != this.tile.SLOT_INVENTORY_END) && (this.tile.SLOT_INVENTORY_START != -1) ) {
	            				if (!this.mergeItemStack(stackInSlot, this.tile.SLOT_INVENTORY_START, this.tile.SLOT_INVENTORY_END + 1, false)) {
	                         		return null;
	                         	}
	            				foundSlot = true;
	            				break;
	            			} else if ( (theTargetSlot.isItemValid(stackInSlot) ) 
	            					&& (theTargetSlot.getSlotStackLimit()>1) 
	            					&& ( (!theTargetSlot.getHasStack()) || (theTargetSlot.getStack().stackSize < theTargetSlot.getSlotStackLimit()) ) ){
	            				if (!this.mergeItemStack(stackInSlot, slotNum, slotNum+1, false)) {
	                    			return null;
	                    		}
	            				foundSlot = true;
	                    		break;
	            			}
            			}
            		}
            	}
            	if (!foundSlot) return null;
            }
            
            

            if (stackInSlot.stackSize == 0) {
                    slotObject.putStack(null);
            } else {
                    slotObject.onSlotChanged();
            }
		}
		
		return stack;
	}
}