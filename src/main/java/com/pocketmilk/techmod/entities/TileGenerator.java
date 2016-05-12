package com.pocketmilk.techmod.entities;


import java.util.Iterator;
import java.util.List;

import com.pocketmilk.techmod.blocks.BlockGeneratorCoal;

import net.darkhax.tesla.api.ITeslaHandler;
import net.darkhax.tesla.api.TeslaContainer;
import net.darkhax.tesla.capability.TeslaStorage;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

// Currently, this block doesn't do jack shit sir
public class TileGenerator extends TileEntity implements ITickable, ISidedInventory {
	
	private TeslaContainer container;
	
	private int burnProgress = 0;
	private int lastBurnTime = 0;
	
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
		this.setCapacity(10000);
		this.setInputRate(10);
		this.setOutputRate(30);
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
	
	public long givePower(long tesla, EnumFacing side, boolean simulated) {
		return this.container.givePower(tesla, side, simulated);
	}
	
	public long takePower (long tesla, EnumFacing side, boolean simulated) {
		return this.container.takePower(tesla, side, simulated);
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
	
	public int getSlotBurnTime() {
		if (slots[0]==null) return 0;
		return TileEntityFurnace.getItemBurnTime(slots[0]);
	}
	
	public int getBurnTime() {
		return this.burnProgress;
	}
	
	public int getTotalBurnTime() {
		return this.lastBurnTime;
	}
	
	
	public int getBurnProgressPercent() {
		if(this.burnProgress != 0 && this.lastBurnTime != 0)
				return (int)((this.burnProgress * 100) / this.lastBurnTime);
		else
			return 0;
				
	}
	
	
	public void update() {
		if (!worldObj.isRemote) {
			//System.out.println(this.container.getOutputRate());
			if (!this.isBurning()) {
				if(!(this.getPower() >= this.getCapacity())) {
					if (this.slots[SLOT_FUEL]!=null) {
						if (this.isFuel()) {
							this.burnProgress = this.getSlotBurnTime();
							this.lastBurnTime = this.getSlotBurnTime();
							this.slots[SLOT_FUEL].stackSize--;
							if (this.slots[SLOT_FUEL].stackSize==0) {
								this.slots[SLOT_FUEL] = null;
							}
							this.updateState();
						}
					}
				}
			}
			else {
				//System.out.println(this.burnProgress);
				this.burnProgress--;
				if (this.burnProgress<=0) {
					this.burnProgress = 0;
					this.lastBurnTime = 0;
					this.updateState();
				}
				if(this.getPower() >= this.getCapacity()) {
	        		this.setPower(this.getCapacity());
	        	} else {
	        		this.setPower(this.getPower() + this.getInputRate());
	        	}
			}
			this.outputEnergy();
            //System.out.println("I have " + this.getPower() + "/" + this.getCapacity() + " power. I am at " + this.pos.toString());
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		if (oldState.getBlock() != newSate.getBlock())
		{
			return true;
		}
		return false;
	}
	
	public void updateState()
	{
		IBlockState BlockStateContainer = worldObj.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockGeneratorCoal)
		{
			BlockGeneratorCoal blockMachineBase = (BlockGeneratorCoal) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(blockMachineBase.ACTIVE) != this.isBurning()) {
				//System.out.println("Before " + worldObj.getBlockState(pos).getProperties().toString());
				EnumFacing facing = worldObj.getBlockState(pos).getValue(blockMachineBase.FACING);
				IBlockState state = worldObj.getBlockState(pos).withProperty(blockMachineBase.ACTIVE, this.isBurning()).withProperty(blockMachineBase.FACING, facing);
				worldObj.setBlockState(pos, state);
				//System.out.println("After " + worldObj.getBlockState(pos).getProperties().toString());
			}
			
		}
	}
	
	public void outputEnergy() { 
		//Lets go around the world and try and give it to someone!
		if (this.getPower()>0) {
			List<ITeslaHandler> tileTesla = TeslaUtils.getConnectedTeslaHandlers(worldObj, pos);
			if(tileTesla.size() != 0) {
				//System.out.println("Connected tesla container found.");
				for(Iterator<ITeslaHandler> i = tileTesla.iterator(); i.hasNext();) {
					ITeslaHandler tile = i.next();
					long ownPowerSent = this.takePower(this.getOutputRate(), null, false);
					if(ownPowerSent != 0) {
						long powerAccepted = tile.givePower(this.getOutputRate(), EnumFacing.UP, false);
						if(powerAccepted < ownPowerSent) {
							//System.out.println("Power Sent: " + ownPowerSent + "/" + this.getOutputRate() + " - Max Power Accepted: " + powerAccepted);
							//System.out.println("Current power after sending " + ownPowerSent + " = " + this.getPower());
							if((ownPowerSent-powerAccepted) > 0) {
								this.setPower(this.getPower() + (ownPowerSent-powerAccepted));
								//System.out.println("Refunding power " + (ownPowerSent-powerAccepted));
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("burnProgress")) this.burnProgress = compound.getInteger("burnProgress");
		if (compound.hasKey("lastBurnTime")) this.lastBurnTime = compound.getInteger("lastBurnTime");
		 NBTTagList tagList = compound.getTagList("Inventory", 10);
         for (int i = 0; i < tagList.tagCount(); i++) {
                 NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
                 byte slot = tag.getByte("Slot");
                 if (slot >= 0 && slot < slots.length) {
                         slots[slot] = ItemStack.loadItemStackFromNBT(tag);
                 }
         }
		this.container = new TeslaContainer(null, compound.getTag("TeslaContainer"));
	}
	
	@Override
	public void writeToNBT (NBTTagCompound compound) {
		
		super.writeToNBT(compound);
		compound.setInteger("burnProgress", this.burnProgress);
		compound.setInteger("lastBurnTime", this.lastBurnTime);
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < slots.length; i++) {
                ItemStack stack = slots[i];
                if (stack != null) {
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setByte("Slot", (byte) i);
                        stack.writeToNBT(tag);
                        itemList.appendTag(tag);
                }
        }
        compound.setTag("Inventory", itemList);
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