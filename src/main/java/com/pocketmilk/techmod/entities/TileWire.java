package com.pocketmilk.techmod.entities;

import com.pocketmilk.techmod.blocks.BlockWireBase;

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

public class TileWire extends TileEntity implements ITickable {
	
	private TeslaContainer container;
	public boolean connectedNorth = false;
	public boolean connectedSouth = false;
	public boolean connectedEast = false;
	public boolean connectedWest = false;
	public boolean connectedUp = false;
	public boolean connectedDown = false;
	private boolean hasChanged = false;
	
	public TileWire() {
		super();

		this.container = new TeslaContainer();
		this.doInit();
	}
	
	public void doInit() {
		this.setCapacity(512);
		this.setInputRate(16);
		this.setOutputRate(16);
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
		if (compound.hasKey("connectedNorth"))this.connectedNorth = compound.getBoolean("connectedNorth");
		if (compound.hasKey("connectedSouth"))this.connectedSouth = compound.getBoolean("connectedSouth");
		if (compound.hasKey("connectedEast"))this.connectedEast = compound.getBoolean("connectedEast");
		if (compound.hasKey("connectedWest"))this.connectedWest = compound.getBoolean("connectedWest");
		if (compound.hasKey("connectedUp"))this.connectedUp = compound.getBoolean("connectedUp");
		if (compound.hasKey("connectedDown"))this.connectedDown = compound.getBoolean("connectedDown");
		//this.pwrAmount = compound.getInteger("pwrAmount");
		this.container = new TeslaContainer(null, compound.getTag("TeslaContainer"));
	}
	
	@Override
	public void writeToNBT (NBTTagCompound compound) {
		
		super.writeToNBT(compound);
		compound.setBoolean("connectedNorth", this.connectedNorth);
		compound.setBoolean("connectedSouth", this.connectedSouth);
		compound.setBoolean("connectedEast", this.connectedEast);
		compound.setBoolean("connectedWest", this.connectedWest);
		compound.setBoolean("connectedUp", this.connectedUp);
		compound.setBoolean("connectedDown", this.connectedDown);
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


    @Override
    public void update () {
        if (!this.worldObj.isRemote) {
        	boolean tmpconnectedNorth = false;
        	boolean tmpconnectedSouth = false;
        	boolean tmpconnectedEast = false;
        	boolean tmpconnectedWest = false;
        	boolean tmpconnectedUp = false;
        	boolean tmpconnectedDown = false;
            for (final EnumFacing facing : EnumFacing.values()) {
                final TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
                if (tile != null && !tile.isInvalid() && tile.hasCapability(TeslaStorage.TESLA_HANDLER_CAPABILITY, facing)) {
                	//System.out.println("Detected tesla at " + facing.toString());
                    //teslaHandlers.add(tile.getCapability(TeslaStorage.TESLA_HANDLER_CAPABILITY, facing));
                	switch(facing) {
                	case NORTH:
                			tmpconnectedNorth = true;
                			break;
                	case SOUTH:
                			tmpconnectedSouth = true;
                    		break;
                	case EAST:
                			tmpconnectedEast = true;
                        	break;
                	case WEST:
                			tmpconnectedWest = true;
                            break;
                	case UP:
                			tmpconnectedUp = true;
                            break;
                	case DOWN:
                			tmpconnectedDown = true;
                            break;
                	}
                }
            }
            if(tmpconnectedNorth != this.connectedNorth
            		|| tmpconnectedSouth != this.connectedSouth
            		|| tmpconnectedEast != this.connectedEast
            		|| tmpconnectedWest != this.connectedWest
            		|| tmpconnectedUp != this.connectedUp
            		|| tmpconnectedDown != this.connectedDown) {
            	this.hasChanged = true;
            	this.connectedNorth = tmpconnectedNorth;
            	this.connectedSouth = tmpconnectedSouth;
            	this.connectedEast = tmpconnectedEast;
            	this.connectedWest = tmpconnectedWest;
            	this.connectedUp = tmpconnectedUp;
            	this.connectedDown = tmpconnectedDown;
            	//System.out.println("Tesla container detected?");
            	this.updateState();
            }
        }
    }
    
	public void updateState()
	{
		if(this.hasChanged) {
			IBlockState BlockStateContainer = worldObj.getBlockState(pos);
			if (BlockStateContainer.getBlock() instanceof BlockWireBase)
			{
				BlockWireBase blockMachineBase = (BlockWireBase) BlockStateContainer.getBlock();
				IBlockState state = worldObj.getBlockState(pos)
						.withProperty(blockMachineBase.NORTH, this.connectedNorth)
						.withProperty(blockMachineBase.SOUTH, this.connectedSouth)
						.withProperty(blockMachineBase.EAST, this.connectedEast)
						.withProperty(blockMachineBase.WEST, this.connectedWest)
						.withProperty(blockMachineBase.UP, this.connectedUp)
						.withProperty(blockMachineBase.DOWN, this.connectedDown);
				worldObj.setBlockState(pos, state);
			}
			this.hasChanged = false;
		}
	}
}
