package com.pocketmilk.techmod.entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

// Currently, this block doesn't do jack shit sir
public class TileGenerator extends BaseTile {
	protected int burnProgress = 0;
	

	//protected ItemStack[] slots;
	
	public TileGenerator() {
		super(0);
	}
	
	public TileGenerator(int numSlots) {
		super(numSlots);
	}
	
	// Test if the generator currently has some currently burning progress left
	public boolean isBurning() {
		return (burnProgress>0);
	}
	
	// Inventory handling functions -----
	// ----------------------------------
	// Not sure what this one is used for
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] output = new int[slots.length];
		for (int i=0; i<slots.length; i++) {
			output[i] = i;
		}
		return output;
	}
	
	// This is used to test if a specific item can be extracted from a specific slot on a specific face
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing face) {
		//if (sides[face.ordinal()] == WrenchModes.Mode.Disabled) return false;
		//if ( (slot>=SLOT_INVENTORY_START) && (slot<=SLOT_INVENTORY_END) ) {
		//	if ( (sides[face.ordinal()] == WrenchModes.Mode.Normal) || (sides[face.ordinal()] == WrenchModes.Mode.Output) )
		//		return true;
		//}
		return true;
	}
	
	// Simple test to see if an item can be inserted into a slot, based on if it's the same item, if there's room, or if it's valid
	// We can also add conditions where only certain items can be inserted from a certain side
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing face) {
		if ( (slots[slot] != null) 
				&& (slots[slot].isItemEqual(stack))
				&& (ItemStack.areItemStackTagsEqual(stack, slots[slot])) ) {
			int availSpace = this.getInventoryStackLimit() - slots[slot].stackSize;
			if (availSpace>0) {
				return true;
			}
		} else if (isItemValidForSlot(slot, stack)) {
			return true;
		}
		return false;
	}
	// Can test if an item is a valid type we want to specify for any specific slot, such as fuel only in slot 1 etc
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;//this.isItemValidForSlot(slot, stack, false);
	}
	
	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		if (slots[slot] != null) {
			ItemStack newStack;
			if (slots[slot].stackSize <= amt) {
				newStack = slots[slot];
				slots[slot] = null;
			} else {
				newStack = slots[slot].splitStack(amt);
				if (slots[slot].stackSize == 0) {
					slots[slot] = null;
				}
			}
			return newStack;
		}
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (slots[slot]!=null) {
			ItemStack stack = slots[slot];
			slots[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	
	@Override
	public void clear() {
		for (int i = 0; i < this.slots.length; ++i) {
            this.slots[i] = null;
        }
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(getPos()) == this &&
		 player.getDistanceSq(getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5) < 64;
	}
	
	
	
}