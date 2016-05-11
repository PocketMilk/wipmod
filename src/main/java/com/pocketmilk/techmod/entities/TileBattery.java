package com.pocketmilk.techmod.entities;

import com.pocketmilk.techmod.blocks.BlockBattery;

import net.darkhax.tesla.Tesla;
import net.darkhax.tesla.api.TeslaContainer;
import net.darkhax.tesla.capability.TeslaStorage;
import net.darkhax.tesla.lib.TeslaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileBattery extends TileEntity implements ITickable {
	
	private TeslaContainer container;
	private int pwrAmount = 0;
	
	public TileBattery() {
		super();

		this.container = new TeslaContainer();
		this.doInit();
	}
	
	public void doInit() {
		setCapacity(1000);
	}
	
	public boolean hasPower() {
		return (this.getPower()>0);
	}
	
	public void setPower(long value) {
		this.container.setPower(value);
	}
	
	public int getPwrAmount() {
		return this.pwrAmount;
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

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.pwrAmount = compound.getInteger("pwrAmount");
		this.container = new TeslaContainer(null, compound.getTag("TeslaContainer"));
	}
	
	@Override
	public void writeToNBT (NBTTagCompound compound) {
		
		super.writeToNBT(compound);
		compound.setInteger("pwrAmount", this.pwrAmount);
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
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		if (oldState.getBlock() != newSate.getBlock())
		{
			return true;
		}
		return false;
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
        		this.updateState(4);
        	} else {
        		this.setPower(this.getPower() + 1);
        		//System.out.println("I have " + this.container.getStoredPower(EnumFacing.UP) + "/" + this.container.getCapacity(EnumFacing.UP) + " power. I am at " + this.pos.toString());
            	//System.out.println("Percentage = " + getPercentStorage());
        	}
        	if(this.getPercentStorage() == 0) {
        		this.updateState(0);
        	}
        	else if(this.getPercentStorage() == 25 || this.getPercentStorage() == 50 || this.getPercentStorage() == 75) {
        		this.updateState((int)this.getPercentStorage()/25);
        	}
        }
    }
    
	public void updateState(int amount)
	{
		this.pwrAmount = amount;
		IBlockState BlockStateContainer = worldObj.getBlockState(pos);
		if (BlockStateContainer.getBlock() instanceof BlockBattery)
		{
			BlockBattery blockMachineBase = (BlockBattery) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(blockMachineBase.PERCENT) != amount) {
				System.out.println("Before " + worldObj.getBlockState(pos).getProperties().toString());
				EnumFacing facing = worldObj.getBlockState(pos).getValue(blockMachineBase.FACING);
				IBlockState state = worldObj.getBlockState(pos).withProperty(blockMachineBase.PERCENT, amount).withProperty(blockMachineBase.FACING, facing);
				worldObj.setBlockState(pos, state);
				System.out.println("After " + worldObj.getBlockState(pos).getProperties().toString());
			}
			
		}
	}
}
