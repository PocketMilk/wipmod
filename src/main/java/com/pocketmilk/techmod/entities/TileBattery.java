package com.pocketmilk.techmod.entities;

import net.darkhax.tesla.Tesla;
import net.darkhax.tesla.api.TeslaContainer;
import net.darkhax.tesla.capability.TeslaStorage;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileBattery extends TileEntity implements ITickable {
	
	private TeslaContainer container;
	
	public TileBattery() {
		super();

		this.container = new TeslaContainer();
	}
	
	public boolean hasPower() {
		return (this.getPower()>0);
	}
	
	public void setPower(long value) {
		this.container.setPower(value);
	}
	
	public long getPower() {
		return this.container.getStoredPower(EnumFacing.UP);
	}
	
	public long getCapacity() {
		return this.container.getCapacity(EnumFacing.UP);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		this.container = new TeslaContainer(null, compound.getTag("TeslaContainer"));
	}
	
	@Override
	public void writeToNBT (NBTTagCompound compound) {
		
		super.writeToNBT(compound);
		
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
	
	public long getPercentStorage() {
		long curPower = this.getPower();
		long maxPower = this.getCapacity();
		
		return (curPower * 100) / maxPower;
	}
	
	public int getEnergyScaled(int scale)
	{
		return (int) ((getPower() * scale / getCapacity()));
	}

    @Override
    public void update () {
        
        // This tile entity will spam the console with how much power it has every tick. In
        // other tile entities this could be used to send power to a tile, or take it from
        // another tile. This isn't required for the actual Tesla tile.
        if (!this.worldObj.isRemote) {
        	if(this.getPower() >= this.getCapacity()) {
        		this.setPower(this.getCapacity());
        	} else {
        		this.setPower(this.getPower() + 1);
        		//System.out.println("I have " + this.container.getStoredPower(EnumFacing.UP) + "/" + this.container.getCapacity(EnumFacing.UP) + " power. I am at " + this.pos.toString());
            	//System.out.println("Percentage = " + getPercentStorage());
        	}
        }
    }
}
