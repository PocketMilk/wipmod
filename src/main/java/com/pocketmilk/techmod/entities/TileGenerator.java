package com.pocketmilk.techmod.entities;


import net.darkhax.tesla.api.ITeslaHandler;
import net.darkhax.tesla.api.TeslaContainer;
import net.darkhax.tesla.capability.TeslaStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;

// Currently, this block doesn't do jack shit sir
public class TileGenerator extends TileEntity implements ITickable, ISidedInventory {
	
	private TeslaContainer container;
	
	public int burnProgress = 0;
	public int lastBurnTime = 0;
	
	public ItemStack[] slots;
	public int SLOT_INVENTORY_START = -1;
	public int SLOT_INVENTORY_END = -1;
	public int SLOT_FUEL = 0;
	
	private int numSlots = 0;
	

	//protected ItemStack[] slots;
	public TileGenerator() {
		super();
		
		this.container = new TeslaContainer();
		
		slots = new ItemStack[1];
		if (numSlots > 9) {
			SLOT_INVENTORY_START = numSlots - 8;
			SLOT_INVENTORY_END = numSlots;
			//ProgressiveAutomation.logger.info("Start: "+SLOT_INVENTORY_START+" End: "+SLOT_INVENTORY_END);
		} else {
			SLOT_INVENTORY_START = SLOT_INVENTORY_END = numSlots;
		}
		Init();
	}
	
	private void Init() {
		this.setCapacity(6000);
		this.setInputRate(10);
		this.setOutputRate(10);
	}
	
	public long getPercentStorage() {
		long curPower = this.container.getStoredPower(EnumFacing.UP);
		long maxPower = this.container.getCapacity(EnumFacing.UP);
		//System.out.println(curPower + "  " + maxPower);
		return (curPower * 100) / maxPower;
	}
	
	public boolean hasPower() {
		return (this.getPower()>0);
	}
	
	public void setPower(long value) {
		this.container.setPower(value);
	}
	
	public void setBurnTime(int value) {
		this.burnProgress = value;
	}
	
	public void setTotalBurnTime(int value) {
		this.lastBurnTime = value;
	}
	
	public long getPower() {
		return this.container.getStoredPower(EnumFacing.UP);
	}
	
	public long getCapacity() {
		return this.container.getCapacity(EnumFacing.UP);
	}
	
	public void givePower(long tesla, EnumFacing side, boolean simulated) {
		this.container.givePower(tesla, side, simulated);
	}
	
	public void takePower (long tesla, EnumFacing side, boolean simulated) {
		this.container.takePower(tesla, side, simulated);
	}
	
	public void setCapacity(long capacity) {
		 this.container.setCapacity(capacity);
	}
	
	public long getInputRate () {
        
        return this.container.getInputRate();
    }
	
	public void setInputRate (long rate) {
        
        this.container.setInputRate(rate);
    }
	
    public long getOutputRate () {
        
        return this.container.getOutputRate();
    }
    
    public void setOutputRate (long rate) {
        
        this.container.setOutputRate(rate);
    }
    
    public boolean isInputSide (EnumFacing side) {
        
        return this.container.isInputSide(side);
    }
    
    public boolean isOutputSide (EnumFacing side) {
        
        return this.container.isOutputSide(side);
    }
	
	// Test if the generator currently has some currently burning progress left
	public boolean isBurning() {
		return (this.burnProgress>0);
	}
	
	// Inventory handling functions -----
	// ----------------------------------
	// Not sure what this one is used for
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] output = new int[this.slots.length];
		for (int i=0; i<this.slots.length; i++) {
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
	
	public boolean isFuel() {
		return (TileEntityFurnace.getItemBurnTime(slots[0])>0);
	}
	
	public int getBurnTime() {
		if (slots[0]==null) return 0;
		return TileEntityFurnace.getItemBurnTime(slots[0]);
	}
	
	public int getTotalBurnTime() {
		return this.lastBurnTime;
	}
	
	
	public int getBurnProgressPercent() {
		if(isBurning()) {
			if(burnProgress != 0 && lastBurnTime != 0) {
				return (int)((burnProgress * 100) / lastBurnTime);
				
			}
			else
				return 0;
			
		}
		else
			return 0;
	}
	
	
	public void update() {
		if (!worldObj.isRemote) {
			if (!this.isBurning()) {
						if (this.slots[SLOT_FUEL]!=null) {
							if (this.isFuel()) {
								this.burnProgress = this.getBurnTime();
								this.lastBurnTime = this.getBurnTime();
									this.slots[SLOT_FUEL].stackSize--;
									if (this.slots[SLOT_FUEL].stackSize==0) {
										this.slots[SLOT_FUEL] = null;
									}
								}
							}
						}
			 else {
				this.burnProgress--;
				if (this.burnProgress<=0) {
					this.burnProgress = 0;
					this.lastBurnTime = 0;
				}
				if(this.getPower() >= this.getCapacity()) {
	        		this.setPower(this.getCapacity());
	        	} else {
	        		this.setPower(this.getPower() + this.getInputRate());
	        	}
			}
			//this.outputEnergy();
            //System.out.println("I have " + this.getPower() + "/" + this.getCapacity() + " power. I am at " + this.pos.toString());
		}
	}
	
	public void outputEnergy() { 
		//Lets go around the world and try and give it to someone!
		for(EnumFacing facing : EnumFacing.values()) {
			//Do we have any energy up for grabs?
			if (getPower()>0) {
				TileEntity entity = worldObj.getTileEntity(pos.offset(facing));
				/*if (entity.hasCapability(TeslaStorage.TESLA_HANDLER_CAPABILITY, EnumFacing.UP)) {
					
					ITeslaHandler entityHandler = entity.getCapability(TeslaStorage.TESLA_HANDLER_CAPABILITY, EnumFacing.UP);
					if (entityHandler.isInputSide(facing.getOpposite())) {
						long giveAmount = entityHandler.givePower(getOutputRate(), facing.getOpposite(), true);
						if (giveAmount>0) {
							entityHandler.givePower(getOutputRate(), facing.getOpposite(), false);
							takePower(getOutputRate(), EnumFacing.UP, false);
						}
					}
				}*/
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("burnProgress")) burnProgress = compound.getInteger("burnProgress");
		if (compound.hasKey("lastBurnTime")) lastBurnTime = compound.getInteger("lastBurnTime");
		this.container = new TeslaContainer(null, compound.getTag("TeslaContainer"));
	}
	
	@Override
	public void writeToNBT (NBTTagCompound compound) {
		
		super.writeToNBT(compound);
		compound.setInteger("burnProgress", burnProgress);
		compound.setInteger("lastBurnTime", lastBurnTime);
		compound.setTag("TeslaContainer", this.container.writeNBT(null));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		
		if (capability == TeslaStorage.TESLA_HANDLER_CAPABILITY)
			return (T) this.container;
		
		return super.getCapability(capability, facing);
	}
	
	@Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
        
        // This works similarly to the getter method above. It just checks to see if the
        // TileEntity has an ITeslaHandler.
        if (capability == TeslaStorage.TESLA_HANDLER_CAPABILITY)
            return true;
            
        return super.hasCapability(capability, facing);
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	// USELESS STUFF BELOW
	// USELESS STUFF BELOW
	// USELESS STUFF BELOW

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}
	
	@Override
	public void openInventory(EntityPlayer playerIn) {}

	@Override
	public void closeInventory(EntityPlayer playerIn) {}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
}